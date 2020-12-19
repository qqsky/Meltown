package meltown;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;

public class Moderation extends ListenerAdapter {
    static Connection connection;
    String guild;
    private static final String DB_URL = "jdbc:sqlite:database.db";
    private static final String DB_DRIVER = "org.sqlite.JDBC";
    static String muteid;
    List<Member> dodiki;
    private static final String PREFIX = "#";

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) throws NullPointerException {
        guild = e.getGuild().getName();
        try {
            connection = DriverManager.getConnection(DB_URL);
            String query = "SELECT muteRoleId FROM SetupDB WHERE GuildName = ?";
            PreparedStatement prst = connection.prepareStatement(query);
            prst.setString(1, guild);
            ResultSet resultSet = prst.executeQuery();
            while (resultSet.next()) {
                muteid = resultSet.getString("muteRoleId");
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        //setup
        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentRaw().startsWith("#setup")) {
                if (e.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                    List<Role> mentionedRoles = e.getMessage().getMentionedRoles();
                    if (!mentionedRoles.isEmpty()) {
                        Role muteRole = mentionedRoles.get(0);
                        muteid = muteRole.getId();
                        try {
                            connection = DriverManager.getConnection(DB_URL);
                            Statement st = connection.createStatement();
                            String query = "INSERT INTO SetupDB(GuildName, muteRoleId)" +
                                    "VAlUES('" + guild + "' , '" + muteid + "')";
                            st.executeUpdate(query);
                            System.out.println("Успешно");
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        EmbedBuilder builder = new EmbedBuilder();
                        builder.setAuthor(e.getAuthor().getName(), e.getAuthor().getAvatarUrl(), e.getAuthor().getEffectiveAvatarUrl());
                        builder.setTitle("Сетап");
                        builder.setDescription("Роль " + muteRole.getAsMention() + " успешно установлена как мьют-роль!");
                        builder.setTimestamp(new Date().toInstant());
                        builder.setFooter("qqsky Dev");
                        builder.setColor(new Color(87, 213, 118));
                        MessageEmbed embed = builder.build();
                        e.getChannel().sendMessage(embed).queue();
                    } else {
                        e.getChannel().sendMessage("Укажите роль для сетапа!").queue();
                    }
                }
                Role role = e.getGuild().getRoleById(muteid);
                dodiki = e.getGuild().getMembersWithRoles(role);
                if (!dodiki.isEmpty()) {
                    if (dodiki.contains(e.getMember())) {
                        e.getMessage().delete().queue();
                    }
                }
                //mute
                if (!e.getAuthor().isBot()) {
                    if (e.getMessage().getContentRaw().startsWith("#mute")) {
                        if (e.getMember().hasPermission(Permission.KICK_MEMBERS)) {
                            String[] args = e.getMessage().getContentRaw().substring(PREFIX.length()).split(" ");
                            String message = e.getMessage().getContentRaw();
                            String sub = "mute ";
                            String sub1 = args[1] + " ";
                            List<Member> mentionedMembers = e.getMessage().getMentionedMembers();
                            if (!mentionedMembers.isEmpty()) {
                                Member toMute = mentionedMembers.get(0);
                                if (muteid.isEmpty()) return;
                                Role muteRole = e.getGuild().getRoleById(muteid);
                                if (toMute.getRoles().contains(muteRole)) {
                                    EmbedBuilder embedMutedBuilder = new EmbedBuilder();
                                    embedMutedBuilder.setColor(new Color(214, 98, 75));
                                    embedMutedBuilder.setTitle("\u274C" + " Ошибка");
                                    embedMutedBuilder.setDescription(toMute.getEffectiveName() + " уже заткнут!");
                                    MessageEmbed muteEmbed = embedMutedBuilder.build();
                                    e.getChannel().sendMessage(muteEmbed).queue();
                                } else if (!toMute.getRoles().contains(muteRole)) {
                                    toMute.getGuild().addRoleToMember(toMute, muteRole).queue();
                                    EmbedBuilder embedMutedBuilder = new EmbedBuilder();
                                    embedMutedBuilder.setColor(new Color(87, 213, 118));
                                    embedMutedBuilder.setTitle("Мут");
                                    embedMutedBuilder.setDescription("`" + toMute.getEffectiveName() + "`" + " был успешно заткнут");
                                    if (args.length >= 3) {
                                        embedMutedBuilder.addField("Причина:", message.substring(sub.length()).substring(sub1.length()), true);
                                    }
                                    embedMutedBuilder.setFooter("Команду вызвал " + e.getAuthor().getName(), e.getJDA().getSelfUser().getAvatarUrl());
                                    MessageEmbed muteEmbed = embedMutedBuilder.build();
                                    e.getChannel().sendMessage(muteEmbed).queue();
                                }
                            }
                        } else {
                            e.getChannel().sendMessage("Укажите пользователя для мута!").queue();
                        }
                    }
                }
                //unmute
                if (!e.getAuthor().isBot()) {
                    if (e.getMessage().getContentRaw().startsWith("#unmute")) {
                        if (e.getMember().hasPermission(Permission.KICK_MEMBERS)) {
                            List<Member> mentionedMembers = e.getMessage().getMentionedMembers();
                            if (!mentionedMembers.isEmpty()) {
                                Member toMute = mentionedMembers.get(0);
                                Role muteRole = e.getGuild().getRoleById(muteid);
                                if (toMute.getRoles().contains(muteRole)) {
                                    toMute.getGuild().removeRoleFromMember(toMute, muteRole).queue();
                                    e.getChannel().sendMessage("Пользователь " + "`" + toMute.getEffectiveName() + "`" + " был успешно размучен").queue();
                                } else if (!toMute.getRoles().contains(muteRole)) {
                                    e.getChannel().sendMessage(toMute.getEffectiveName() + " не имеет запрета!").queue();
                                } else {
                                    e.getChannel().sendMessage("Укажите пользователя для размута!").queue();
                                }
                            }
                        } else {
                            e.getChannel().sendMessage("Недостаток прав!").queue();
                        }
                    }
                }
                //tempmute
                if (!e.getAuthor().isBot()) {
                    if (e.getMessage().getContentRaw().startsWith("#tempmute")) {
                        String[] args = e.getMessage().getContentRaw().substring(PREFIX.length()).split(" ");
                        String message = e.getMessage().getContentRaw();
                        String sub = "tempmute ";
                        String sub1 = args[1] + " ";
                        String sub2 = args[2] + " ";
                        List<Member> mentionedMembers = e.getMessage().getMentionedMembers();
                        if (!mentionedMembers.isEmpty()) {
                            Member toMute = mentionedMembers.get(0);
                            if (muteid.isEmpty()) {
                                e.getChannel().sendMessage("Перед использованием установите мьют-роль!").queue();
                                return;
                            }
                            Role muteRole = e.getGuild().getRoleById(muteid);
                            if (args.length < 3) {
                                e.getChannel().sendMessage("Укажите время мута!").queue();
                                return;
                            } else {
                                if (toMute.getRoles().contains(muteRole)) {
                                    EmbedBuilder embedMutedBuilder = new EmbedBuilder();
                                    embedMutedBuilder.setColor(new Color(214, 98, 75));
                                    embedMutedBuilder.setTitle("\u274C" + " Ошибка");
                                    embedMutedBuilder.setDescription(toMute.getEffectiveName() + " уже заткнут!");
                                    embedMutedBuilder.setFooter("Команду вызвал " + e.getAuthor().getName(), e.getJDA().getSelfUser().getAvatarUrl());
                                    MessageEmbed muteEmbed = embedMutedBuilder.build();
                                    e.getChannel().sendMessage(muteEmbed).queue();
                                } else if (!toMute.getRoles().contains(muteRole)) {
                                    toMute.getGuild().addRoleToMember(toMute, muteRole).queue();
                                    int b = Integer.parseInt(args[2].replace("m", "").replace("h", "").replace("d", ""));
                                    TimeUnit timeticks = null;
                                    if (args[2].endsWith("m")) {
                                        timeticks = TimeUnit.MINUTES;
                                    } else if (args[2].endsWith("h")) {
                                        timeticks = HOURS;
                                    } else if (args[2].endsWith("d")) {
                                        timeticks = DAYS;
                                    }
                                    EmbedBuilder embedMutedBuilder = new EmbedBuilder();
                                    embedMutedBuilder.setColor(new Color(87, 213, 118));
                                    embedMutedBuilder.setTitle("Мут");
                                    embedMutedBuilder.setDescription("`" + toMute.getEffectiveName() + "`" + " был успешно заткнут");
                                    if (args.length >= 4) {
                                        embedMutedBuilder.addField("Причина:", message.substring(sub.length()).substring(sub1.length()).substring(sub2.length()), true);
                                    }
                                    embedMutedBuilder.addField("Время:", args[2], true);
                                    embedMutedBuilder.setFooter("Команду вызвал " + e.getAuthor().getName(), e.getJDA().getSelfUser().getAvatarUrl());
                                    MessageEmbed muteEmbed = embedMutedBuilder.build();
                                    e.getChannel().sendMessage(muteEmbed).queue();
                                    toMute.getGuild().removeRoleFromMember(toMute, muteRole).queueAfter(b, timeticks);
                                }
                            }
                        } else {
                            e.getChannel().sendMessage("Укажите пользователя для мута!").queue();
                        }
                    }
                }
            }
        }
    }
}

