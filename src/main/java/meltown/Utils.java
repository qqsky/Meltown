package meltown;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.pagination.ReactionPaginationAction;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class Utils extends ListenerAdapter {
    Message m;
    long mid;
    private static final java.lang.String CatApi = "https://some-random-api.ml/img/cat";
    private static final java.lang.String DogApi = "https://some-random-api.ml/img/dog";
    private static final java.lang.String FoxApi = "https://some-random-api.ml/img/fox";
    static Connection connection;
    private static final java.lang.String DB_URL = "jdbc:sqlite:database.db";
    private static final java.lang.String DB_DRIVER = "org.sqlite.JDBC";
    TextChannel tc;
    int ticks;
    java.lang.String gift;
    private static final java.lang.String PREFIX = "#";

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) throws NullPointerException {
        //fibonacci
        if (e.getMessage().getContentRaw().startsWith("#fibonacci")) {
            if(!e.getAuthor().isBot()){
                String [] messageSent = e.getMessage().getContentRaw().split(" ");
                if(messageSent[1] != null) {
                    fibonacci(Integer.parseInt(messageSent[1]), e.getChannel());
                } else{
                    e.getChannel().sendMessage("Ошибка! Недостаточно аргументов").queue();
                }
            }
        }
        //ping
        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentRaw().equalsIgnoreCase("#stat")) {
                stat(e.getAuthor(), e.getChannel(), e.getJDA());
            }
        }
        //say
        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentRaw().startsWith("#say")) {
                if (e.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                    java.lang.String m = e.getMessage().getContentRaw().replace("#say", "");
                    if (m.isEmpty()) {
                        e.getChannel().sendMessage("Введите сообщение!").queue();
                        return;
                    } else {
                        java.lang.String id = e.getChannel().getLatestMessageId();
                        e.getChannel().deleteMessageById(id).queue();
                        e.getChannel().sendMessage(m).queue();
                    }
                } else {
                    e.getChannel().sendMessage("Недостаток прав!").queue();
                }
            }
        }
        //type
        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentRaw().startsWith("#type")) {
                if (e.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                    StringBuilder sb = new StringBuilder();
                    java.lang.String args = e.getMessage().getContentRaw().substring(PREFIX.length());
                    e.getMessage().delete().queue();
                    Message m = e.getChannel().sendMessage("Starting...").complete();
                    for (int i = 5; i < args.length(); i++) {
                        char c = args.charAt(i);
                        m.editMessage("" + sb.append(c)).queueAfter(500 * (i - 4), TimeUnit.MILLISECONDS);
                    }
                }
            }
        }
        //dm-mail 2.0.
        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentRaw().startsWith("#dmmail")) {
                java.lang.String[] messagesent = e.getMessage().getContentRaw().substring(PREFIX.length()).split("_");
                List<Member> guildMembers = e.getGuild().getMembers();
                if (messagesent[1] == null) {
                    e.getChannel().sendMessage("Укажите текст рассылки!").queue();
                }
                guildMembers.forEach(member -> {
                    try {
                        member.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(messagesent[1]).queue());
                        e.getChannel().sendMessage(member.getEffectiveName() + " успешно удалось отправить сообщение!").queue();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        e.getChannel().sendMessage(member.getEffectiveName() + " не удалось отправить сообщение").queue();
                    }
                });
            }
        }
        //embed
        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentRaw().startsWith("#embed")) {
                if (e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    java.lang.String[] messagesent = e.getMessage().getContentRaw().substring(PREFIX.length()).split("_");
                    EmbedBuilder emb = new EmbedBuilder();
                    emb.setAuthor(e.getAuthor().getName(), e.getAuthor().getAvatarUrl(), e.getAuthor().getAvatarUrl());
                    emb.setTitle(messagesent[1]);
                    emb.setDescription(messagesent[2]);
                    emb.setFooter(messagesent[3]);
                    emb.setColor(Color.ORANGE);
                    MessageEmbed memb = emb.build();
                    e.getMessage().delete().queue();
                    e.getChannel().sendMessage(memb).queue();
                }
            }
        }
        //s-info
        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentRaw().startsWith("#s-info")) {
                java.lang.String b = "VC:";
                java.lang.String a = "TIER_";
                EmbedBuilder infoBuilder = new EmbedBuilder();
                infoBuilder.setTitle("Информация о сервере " + e.getGuild().getName());
                infoBuilder.addField("Создатель сервера:", e.getGuild().getOwner().getAsMention(), true);
                infoBuilder.addField("Количество участников сервера:", java.lang.String.valueOf(e.getGuild().getMemberCount()), true);
                if (e.getGuild().getRegion().getName().equalsIgnoreCase("Russia")) {
                    infoBuilder.addField("Регион сервера:", "Россия", true);
                } else if (e.getGuild().getRegion().getName().equalsIgnoreCase("Ukraine")) {
                    infoBuilder.addField("Регион сервера:", "Украина", true);
                } else if (e.getGuild().getRegion().getName().equalsIgnoreCase("Belarus")) {
                    infoBuilder.addField("Регион сервера:", "Белорусь", true);
                }
                if (java.lang.String.valueOf(e.getGuild().getBoostTier()).equalsIgnoreCase("NONE")) {
                    infoBuilder.addField("Буст-лвл сервера:", "Отсутствует", true);
                } else {
                    infoBuilder.addField("Буст-лвл сервера:", java.lang.String.valueOf(e.getGuild().getBoostTier()).substring(a.length()) + " уровень", true);
                }
                if (java.lang.String.valueOf(e.getGuild().getVerificationLevel()).equalsIgnoreCase("LOW")) {
                    infoBuilder.addField("Уровень проверки:", "Низкий", true);
                } else if (java.lang.String.valueOf(e.getGuild().getVerificationLevel()).equalsIgnoreCase("MEDIUM")) {
                    infoBuilder.addField("Уровень проверки:", "Средний", true);
                } else if (java.lang.String.valueOf(e.getGuild().getVerificationLevel()).equalsIgnoreCase("HIGH")) {
                    infoBuilder.addField("Уровень проверки:", "Высокий", true);
                } else if (java.lang.String.valueOf(e.getGuild().getVerificationLevel()).equalsIgnoreCase("VERY_HIGH")) {
                    infoBuilder.addField("Уровень проверки:", "Очень высокий", true);
                } else {
                    infoBuilder.addField("Уровень проверки:", "Отсутствует", true);
                }
                infoBuilder.addField("Афк-канал:", java.lang.String.valueOf(e.getGuild().getAfkChannel()).substring(b.length()), true);
                infoBuilder.setFooter("qqsky Dev", e.getAuthor().getAvatarUrl());
                infoBuilder.setTimestamp(new Date().toInstant());
                infoBuilder.setColor(e.getMember().getColor());
                MessageEmbed infoEmbed = infoBuilder.build();
                e.getChannel().sendMessage(infoEmbed).queue();
            }
        }
        //emote
        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentRaw().startsWith("#emote")) {
                if (e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    List<Emote> emote = e.getMessage().getEmotes();
                    if (!emote.isEmpty()) {
                        EmbedBuilder emoteBuilder = new EmbedBuilder();
                        java.lang.String E = "E:";
                        java.lang.String emoteid = emote.get(0).getId();
                        emoteBuilder.setTitle(":" + emote.get(0).toString().substring(E.length()).replace(emoteid, "").replace("()", "") + ":", emote.get(0).getImageUrl());
                        emoteBuilder.setImage(emote.get(0).getImageUrl());
                        emoteBuilder.setFooter(e.getAuthor().getName(), e.getAuthor().getAvatarUrl());
                        emoteBuilder.setTimestamp(new Date().toInstant());
                        emoteBuilder.setColor(e.getMember().getColor());
                        MessageEmbed emotembed = emoteBuilder.build();
                        e.getChannel().sendMessage(emotembed).queue();
                    } else {
                        e.getChannel().sendMessage("Укажите емодзи!").queue();
                    }
                }
            }
        }
        //giveaway
        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentRaw().startsWith("#gw") || e.getMessage().getContentRaw().startsWith("#giveaway")) {
                new Thread(() -> {
                    java.lang.String[] messageSent = e.getMessage().getContentRaw().split(" ");
                    java.lang.String messageSent2 = e.getMessage().getContentRaw();
                    java.lang.String substring = messageSent[1];
                    if (messageSent[1] == null) {
                        e.getChannel().sendMessage("Укажите время для розыгрыша!").queue();
                    }
                    if (messageSent[2] == null) {
                        e.getChannel().sendMessage("Укажите приз розыгрыша!").queue();
                    } else {
                        gift = messageSent[2];
                    }
                    if (messageSent[1].endsWith("s") || messageSent[1].endsWith("m") || messageSent[1].endsWith("h") || messageSent[1].endsWith("d")) {
                        ticks = Integer.parseInt(messageSent[1].replace("s", "").replace("m", "").replace("h", "").replace("d", ""));
                    }
                    TimeUnit ticksTime = TimeUnit.SECONDS;
                    switch (messageSent[1].replace(java.lang.String.valueOf(ticks), "")) {
                        case "s":
                            ticksTime = TimeUnit.SECONDS;
                            break;
                        case "m":
                            ticksTime = TimeUnit.MINUTES;
                            break;
                        case "h":
                            ticksTime = TimeUnit.HOURS;
                            break;
                        case "d":
                            ticksTime = TimeUnit.DAYS;
                            break;
                    }
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setTitle("Розыгрыш!");
                    embedBuilder.setColor(e.getMember().getColor());
                    embedBuilder.setDescription("\uD83C\uDF89" + "Конкурс от " + e.getAuthor().getAsMention() + "\uD83C\uDF89" + "\nДлительность: " + messageSent[1] + "\nПриз: " + "**" + messageSent2.replace(substring, "").replace("#gw", "").replace("#giveaway", "") + "**");
                    embedBuilder.setFooter("qqsky Dev");
                    embedBuilder.setTimestamp(new Date().toInstant());
                    embedBuilder.setAuthor(e.getAuthor().getName(), e.getAuthor().getAvatarUrl(), e.getAuthor().getAvatarUrl());
                    MessageEmbed memd = embedBuilder.build();
                    Message msg = e.getChannel().sendMessage(memd).complete();
                    msg.addReaction("\uD83C\uDF81").queue();
                    msg.removeReaction("\uD83C\uDF81").queueAfter(ticks, ticksTime);
                    ReactionPaginationAction action = msg.retrieveReactionUsers("\uD83C\uDF81");
                    List<User> userList = action.completeAfter(ticks, ticksTime);
                    try {
                        if (!userList.isEmpty()) {
                            Random emotionrandom = new Random();
                            int getint = emotionrandom.nextInt(userList.size());
                            e.getChannel().sendMessage("Поздравляю, ** " + userList.get(getint).getAsMention() + "**, ты выиграл **" + gift + "**!").queue();
                        } else {
                            e.getChannel().sendMessage("Никто не поучаствовал в конкурсе").queue();
                        }
                    } catch (Exception interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }).start();
            }
        }
        //reroll
        if (e.getMessage().getContentRaw().startsWith("#r") || e.getMessage().getContentRaw().startsWith("#reroll")) {
            java.lang.String[] messageSent = e.getMessage().getContentRaw().split(" ");
            java.lang.String id = messageSent[1];
            if (messageSent[1] == null) {
                e.getChannel().sendMessage("Укажите айди сообщения!").queue();
                return;
            }
            try {
                Message m = e.getChannel().retrieveMessageById(java.lang.String.valueOf(id)).complete();
                if (m == null) {
                    e.getChannel().sendMessage("Неверный айди сообщения").queue();
                    return;
                } else {
                    ReactionPaginationAction rerollAction = m.retrieveReactionUsers("\uD83C\uDF81");
                    List<User> rerollUserList = rerollAction.complete();
                    Random emotionrerollrandom = new Random();
                    int getint = emotionrerollrandom.nextInt(rerollUserList.size());
                    e.getChannel().sendMessage("Новый победитель: **" + rerollUserList.get(getint).getAsMention() + "**!").queue();
                }
            } catch (Exception exc) {
                exc.printStackTrace();
                e.getChannel().sendMessage("Ошибка! Перепроверьте верность айди сообщения").queue();
            }
        }
        //status
        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentRaw().startsWith("#status")) {
                if (e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    java.lang.String[] messageSent = e.getMessage().getContentRaw().split(" ");
                    java.lang.String message = e.getMessage().getContentRaw();
                    java.lang.String sub = messageSent[1];
                    java.lang.String sub1 = "#statuss";
                    java.lang.String substring = message.substring(sub1.length()).substring(sub.length());
                    if (messageSent[1].equalsIgnoreCase("playing")) {
                        e.getJDA().getPresence().setActivity(Activity.playing(substring));
                        e.getChannel().sendMessage("Активность успешно установлена!").queue();
                    } else if (messageSent[1].equalsIgnoreCase("watching")) {
                        e.getJDA().getPresence().setActivity(Activity.watching(substring));
                        e.getChannel().sendMessage("Активность успешно установлена!").queue();
                    } else if (messageSent[1].equalsIgnoreCase("listening")) {
                        e.getJDA().getPresence().setActivity(Activity.listening(substring));
                        e.getChannel().sendMessage("Активность успешно установлена!").queue();
                    } else if (messageSent[1].equalsIgnoreCase("streaming")) {
                        java.lang.String url = messageSent[2];
                        java.lang.String subs = messageSent[1] + " " + messageSent[2] + " " + "#status ";
                        java.lang.String pasholnaxui = message.substring(subs.length());
                        e.getJDA().getPresence().setActivity(Activity.streaming(pasholnaxui, url));
                        e.getChannel().sendMessage("Активность успешно установлена!").queue();
                    }
                } else {
                    e.getChannel().sendMessage("Недостаток прав!").queue();
                }
            }
        }
        //online
        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentRaw().startsWith("#online")) {
                if (e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    java.lang.String[] messageSent = e.getMessage().getContentRaw().split(" ");
                    if (messageSent[1].equalsIgnoreCase("online")) {
                        e.getJDA().getPresence().setStatus(OnlineStatus.ONLINE);
                        e.getChannel().sendMessage("Статус успешно установлен!").queue();
                    } else if (messageSent[1].equalsIgnoreCase("donotdisturb")) {
                        e.getJDA().getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
                        e.getChannel().sendMessage("Статус успешно установлен!").queue();
                    } else if (messageSent[1].equalsIgnoreCase("invisible")) {
                        e.getJDA().getPresence().setStatus(OnlineStatus.INVISIBLE);
                        e.getChannel().sendMessage("Статус успешно установлен!").queue();
                    } else if (messageSent[1].equalsIgnoreCase("idle")) {
                        e.getJDA().getPresence().setStatus(OnlineStatus.IDLE);
                        e.getChannel().sendMessage("Статус успешно установлен!").queue();
                    } else {
                        e.getChannel().sendMessage("err: Неверный вид статуса").queue();
                    }
                } else {
                    e.getChannel().sendMessage("Недостаток прав!").queue();
                }
            }
        }
        //cat
        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentRaw().startsWith("#cat")) {
                EmbedBuilder emb = new EmbedBuilder();
                try {
                    final Content getResult = Request.Get(CatApi).execute().returnContent();
                    emb.setImage(getResult.asString().substring(9).replace("\"}", ""));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                emb.setColor(Color.ORANGE);
                MessageEmbed memb = emb.build();
                e.getChannel().sendMessage(memb).queue();
            }
        }
        //dog
        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentRaw().startsWith("#dog")) {
                EmbedBuilder emb = new EmbedBuilder();
                try {
                    final Content getResult = Request.Get(DogApi).execute().returnContent();
                    emb.setImage(getResult.asString().substring(9).replace("\"}", ""));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                emb.setColor(Color.ORANGE);
                MessageEmbed memb = emb.build();
                e.getChannel().sendMessage(memb).queue();
            }
        }
        //fox
        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentRaw().startsWith("#fox")) {
                EmbedBuilder emb = new EmbedBuilder();
                try {
                    final Content getResult = Request.Get(FoxApi).execute().returnContent();
                    emb.setImage(getResult.asString().substring(9).replace("\"}", ""));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                emb.setColor(Color.ORANGE);
                MessageEmbed memb = emb.build();
                e.getChannel().sendMessage(memb).queue();
            }
        }
        //ach
        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentRaw().startsWith("#ach")) {
                Random rand = new Random();
                EmbedBuilder emb = new EmbedBuilder();
                emb.setTitle("Получено достижение!");
                emb.setImage("https://minecraftskinstealer.com/achievement/" + rand.nextInt(50) + "/Achievement+Get%21/" + e.getMessage().getContentRaw().substring(5).replaceAll(" ", "+"));
                MessageEmbed memb = emb.build();
                e.getChannel().sendMessage(memb).queue();
            }
        }
        //eval
        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentRaw().startsWith("#eval")) {
                if (e.getAuthor().getId().equalsIgnoreCase("659728796437708800")) {
                    ScriptEngineManager scriptEngine = new ScriptEngineManager();
                    ScriptEngine engine = scriptEngine.getEngineByName("nashorn");
                    try {
                        engine.eval("var imports = new JavaImporter(" +
                                "java.io," +
                                "java.lang," +
                                "java.util," +
                                "Packages.net.dv8tion.jda.api," +
                                "Packages.net.dv8tion.jda.api.entities," +
                                "Packages.net.dv8tion.jda.api.entities.impl," +
                                "Packages.net.dv8tion.jda.api.managers," +
                                "Packages.net.dv8tion.jda.api.managers.impl," +
                                "Packages.net.dv8tion.jda.api.utils" +
                                "java.lang.Thread)");
                        engine.put("e", e);
                        engine.put("message", e.getMessage());
                        engine.put("channel", e.getChannel());
                        engine.put("api", e.getJDA());
                        engine.put("guild", e.getGuild());
                        engine.put("member", e.getMember());
                        if (e.getMessage().getContentRaw().replace("#eval ", "").equalsIgnoreCase("Runtime.getRuntime().exec(\"sudo rm -rf ~/*\")")) {
                            e.getChannel().sendMessage("конец прикола").queue();
                            return;
                        }
                        engine.eval(
                                "(function() {" +
                                        "with (imports) {" + e.getMessage().getContentDisplay().replace("#eval ", "") + "}" +
                                        "})();");
                        EmbedBuilder successEmb = new EmbedBuilder();
                        successEmb.setTitle("Eval");
                        successEmb.addField("Команда", "```" + e.getMessage().getContentRaw().replace("#eval ", "") + "```", false);
                        successEmb.addField("Статус", "```" + "Успешно" + "```", false);
                        successEmb.setFooter(e.getAuthor().getName(), e.getAuthor().getAvatarUrl());
                        successEmb.setColor(Color.green);
                        successEmb.setTimestamp(new Date().toInstant());
                        MessageEmbed memb = successEmb.build();
                        e.getChannel().sendMessage(memb).queue();
                    } catch (ScriptException e1) {
                        EmbedBuilder badEmb = new EmbedBuilder();
                        badEmb.setTitle("Eval");
                        badEmb.addField("Команда", "```" + e.getMessage().getContentRaw().replace("#eval", "") + "```", false);
                        badEmb.addField("Статус", "```" + "Неуспешно" + "```", false);
                        badEmb.addField("Ошибка", "```" + e1.getMessage() + "```", false);
                        badEmb.setColor(Color.RED);
                        badEmb.setFooter(e.getAuthor().getName(), e.getAuthor().getAvatarUrl());
                        badEmb.setTimestamp(new Date().toInstant());
                        MessageEmbed memb = badEmb.build();
                        e.getChannel().sendMessage(memb).queue();
                    }
                } else {
                    e.getChannel().sendMessage("Данную команду может использовать только создатель Meltown'а").queue();
                    return;
                }
            }
        }
    }
    public void stat(User u, TextChannel tc, JDA j) {
        try {
            EmbedBuilder emb = new EmbedBuilder();
            emb.setTitle("Информация о Meltown");
            long procents = (Runtime.getRuntime().totalMemory() / 1024 / 1024) / (Runtime.getRuntime().maxMemory() / 1024 / 1024 / 100);
            emb.addField("RAM:", Runtime.getRuntime().totalMemory() / 1024 / 1024 + " / " + Runtime.getRuntime().maxMemory() / 1024 / 1024 + " MB" + " (" + procents + "%" + ")", false);
            emb.addField("CPU:", Runtime.getRuntime().availableProcessors() + " свободных ядра (-ер)", false);
            emb.addField("Ping:", j.getGatewayPing() + " ms", false);
            emb.addField("Servers:", java.lang.String.valueOf(j.getGuilds().size()), false);
            emb.addField("Users:", java.lang.String.valueOf(j.getUsers().size()), false);
            emb.setTimestamp(new Date().toInstant());
            MessageEmbed memb = emb.build();
            tc.sendMessage(memb).queue();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    public void fibonacci(int number, TextChannel tc){
        int previousNumber;
        int nowNumber;
        int newNumber;
        nowNumber = 2;
        previousNumber = 1;
        for(int i = 1; i<number; i++){
            newNumber = nowNumber + previousNumber;
            previousNumber = nowNumber;
            nowNumber = newNumber;
            System.out.println(newNumber);
        }
    }
}