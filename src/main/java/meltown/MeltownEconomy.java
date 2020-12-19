package meltown;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.*;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class MeltownEconomy extends ListenerAdapter {
    String author;
    String mentioned;
    static Connection connection;
    String timetransaction;
    int transaction;
    int transactions;
    List<Member> mentionedMembers;
    double bal;
    double authorbal;
    double autholrmlc;
    double mlc;
    int b;
    Message msg;
    String msgid;
    private static final String DB_URL = "jdbc:sqlite:database.db";
    private static final String DB_DRIVER = "org.sqlite.JDBC";

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentRaw().startsWith("#money")) {
                try {
                    EmbedBuilder emb = new EmbedBuilder();
                    List<Member> mentionedMembers = e.getMessage().getMentionedMembers();
                    if (mentionedMembers.isEmpty()) {
                        author = e.getAuthor().getId();
                        connection = DriverManager.getConnection(DB_URL);
                        String query = "SELECT * FROM EconomyDb WHERE USERID = ?";
                        PreparedStatement prst = connection.prepareStatement(query);
                        prst.setString(1, author);
                        ResultSet resultSet = prst.executeQuery();
                        while (resultSet.next()) {
                            bal = resultSet.getDouble("BAL");
                            mlc = resultSet.getDouble("MELCOIN");
                        }
                        int stackbal = (int) ((bal - bal % 64) / 64);
                        double balost = bal % 64;
                        double lastcharacter = stackbal % 10;
                        if ( mlc != 0 || bal != 0) {
                            if (bal >= 64) {
                                if (lastcharacter == 1) {
                                    if (transactions >= 1) {
                                        emb.setTitle("\uD83D\uDCB3" + " Кошелек " + e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator());
                                        emb.addField("Баланс кошелька", "**" + stackbal + " стак и " + String.format("%.1f", balost) + "** <:diamond_ore:778675508069269504>\nMLC: " + String.format("%.1f", mlc), false);
                                        emb.addField("Последняя транзакция на сервере", timetransaction + " на сумму **" + transaction + "** <:diamond_ore:778675508069269504> ", false);
                                        emb.setFooter("MeltownEconomy");
                                        MessageEmbed memb = emb.build();
                                        e.getChannel().sendMessage(memb).queue();
                                    } else {
                                        emb.setTitle("\uD83D\uDCB3" + " Кошелек " + e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator());
                                        emb.addField("Баланс кошелька", "**" + stackbal + " стак и " + String.format("%.1f", balost) + "** <:diamond_ore:778675508069269504>\nMLC:  " + String.format("%.1f", mlc), false);
                                        emb.setFooter("MeltownEconomy");
                                        MessageEmbed memb = emb.build();
                                        e.getChannel().sendMessage(memb).queue();
                                    }
                                } else if (lastcharacter == 2 || lastcharacter == 3 || lastcharacter == 4) {
                                    if (transactions >= 1) {
                                        emb.setTitle("\uD83D\uDCB3" + " Кошелек " + e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator());
                                        emb.addField("Баланс кошелька", "**" + stackbal + " стака и " + String.format("%.1f", balost) + "** <:diamond_ore:778675508069269504>", false);
                                        emb.addField("Последняя транзакция на сервере", timetransaction + " на сумму **" + transaction + "** <:diamond_ore:778675508069269504>", false);
                                        emb.setFooter("MeltownEconomy");
                                        MessageEmbed memb = emb.build();
                                        e.getChannel().sendMessage(memb).queue();
                                    } else {
                                        emb.setTitle("\uD83D\uDCB3" + " Кошелек " + e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator());
                                        emb.addField("Баланс кошелька", "**" + stackbal + " стака и " + String.format("%.1f", balost) + "** <:diamond_ore:778675508069269504> \nMLC:  " + String.format("%.1f", mlc), false);
                                        emb.setFooter("MeltownEconomy");
                                        MessageEmbed memb = emb.build();
                                        e.getChannel().sendMessage(memb).queue();
                                    }
                                } else {
                                    if (transactions >= 1) {
                                        emb.setTitle("\uD83D\uDCB3" + " Кошелек " + e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator());
                                        emb.addField("Баланс кошелька", "**" + stackbal + " стаков и " + String.format("%.1f", balost) + "** <:gold_ore:773869500948938752>\nMLC: " + String.format("%.1f", mlc), false);
                                        emb.addField("Последняя транзакция на сервере", timetransaction + " на сумму **" + transaction + "** <:diamond_ore:778675508069269504>", false);
                                        emb.setFooter("MeltownEconomy");
                                        MessageEmbed memb = emb.build();
                                        e.getChannel().sendMessage(memb).queue();
                                    } else {
                                        emb.setTitle("\uD83D\uDCB3" + " Кошелек " + e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator());
                                        emb.addField("Баланс кошелька", "**" + stackbal + " стаков и " + String.format("%.1f", balost) + "** <:diamond_ore:778675508069269504>\nMLC:  " + String.format("%.1f", mlc), false);
                                        emb.setFooter("MeltownEconomy");
                                        MessageEmbed memb = emb.build();
                                        e.getChannel().sendMessage(memb).queue();
                                    }
                                }
                            } else {
                                if (transactions >= 1) {
                                    emb.setTitle("\uD83D\uDCB3" + " Кошелек " + e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator());
                                    emb.addField("Баланс кошелька", "**" + bal + "** <:gold_ore:773869500948938752>\nMLC:  " + String.format("%.1f", mlc), false);
                                    emb.addField("Последняя транзакция на сервере", timetransaction + " на сумму **" + transaction + "** <:diamond_ore:778675508069269504> ", false);
                                    emb.setFooter("MeltownEconomy");
                                    MessageEmbed memb = emb.build();
                                    e.getChannel().sendMessage(memb).queue();
                                } else {
                                    emb.setTitle("\uD83D\uDCB3" + " Кошелек " + e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator());
                                    emb.addField("Баланс кошелька", "**" + bal + "** <:diamond_ore:778675508069269504>\nMLC:  " + String.format("%.1f", mlc), false);
                                    emb.setFooter("MeltownEconomy");
                                    MessageEmbed memb = emb.build();
                                    e.getChannel().sendMessage(memb).queue();
                                }
                            }
                        } else {
                            String Query1 = "INSERT INTO EconomyDb(USERID, BAL, MELCOIN)" +
                                    "VALUES('" + author + "' , '" + 0.1 + "' , '" + 0 + "')";
                            try {
                                connection = DriverManager.getConnection(DB_URL);
                                Statement st = connection.createStatement();
                                st.executeUpdate(Query1);
                                e.getChannel().sendMessage("Поздравляем! Это ваш первый вход в виртуальный кошелек, удачных покупок)").queue();
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                    } else {
                        mentioned = mentionedMembers.get(0).getId();
                        connection = DriverManager.getConnection(DB_URL);
                        String query = "SELECT * FROM EconomyDb WHERE USERID = ?";
                        PreparedStatement prst = connection.prepareStatement(query);
                        prst.setString(1, mentioned);
                        ResultSet resultSet = prst.executeQuery();
                        while (resultSet.next()) {
                            bal = resultSet.getDouble("BAL");
                            mlc = resultSet.getDouble("MELCOIN");
                        }
                        int stackbal = (int) ((bal - bal % 64) / 64);
                        double balost = bal % 64;
                        double lastcharacter = stackbal % 10;
                        if (bal != 0 || mlc != 0) {
                            if (bal >= 64) {
                                if (lastcharacter == 1) {
                                    if (transactions >= 1) {
                                        emb.setTitle("\uD83D\uDCB3" + " Кошелек " + mentionedMembers.get(0).getEffectiveName() + "#" + mentionedMembers.get(0).getUser().getDiscriminator());
                                        emb.addField("Баланс кошелька", "**" + stackbal + " стак и " + String.format("%.1f", balost) + "** <:diamond_ore:778675508069269504>\nMLC:  " + String.format("%.1f", mlc), false);
                                        emb.addField("Последняя транзакция на сервере", timetransaction + " на сумму **" + transaction + "** <:diamond_ore:778675508069269504> ", false);
                                        emb.setFooter("MeltownEconomy");
                                        MessageEmbed memb = emb.build();
                                        e.getChannel().sendMessage(memb).queue();
                                    } else {
                                        emb.setTitle("\uD83D\uDCB3" + " Кошелек " + mentionedMembers.get(0).getEffectiveName() + "#" + mentionedMembers.get(0).getUser().getDiscriminator());
                                        emb.addField("Баланс кошелька", "**" + stackbal + " стак и " + String.format("%.1f", balost) + "** <:diamond_ore:778675508069269504>\nMLC:  " + String.format("%.1f", mlc), false);
                                        emb.setFooter("MeltownEconomy");
                                        MessageEmbed memb = emb.build();
                                        e.getChannel().sendMessage(memb).queue();
                                    }
                                } else if (lastcharacter == 2 || lastcharacter == 3 || lastcharacter == 4) {
                                    if (transactions >= 1) {
                                        emb.setTitle("\uD83D\uDCB3" + " Кошелек " + mentionedMembers.get(0).getEffectiveName() + "#" + mentionedMembers.get(0).getUser().getDiscriminator());
                                        emb.addField("Баланс кошелька", "**" + stackbal + " стака и " + String.format("%.1f", balost) + "** <:diamond_ore:778675508069269504>\nMLC:  " + String.format("%.1f", mlc), false);
                                        emb.addField("Последняя транзакция на сервере", timetransaction + " на сумму **" + transaction + "** <:diamond_ore:778675508069269504> ", false);
                                        emb.setFooter("MeltownEconomy");
                                        MessageEmbed memb = emb.build();
                                        e.getChannel().sendMessage(memb).queue();
                                    } else {
                                        emb.setTitle("\uD83D\uDCB3" + " Кошелек " + mentionedMembers.get(0).getEffectiveName() + "#" + mentionedMembers.get(0).getUser().getDiscriminator());
                                        emb.addField("Баланс кошелька", "**" + stackbal + " стака и " + String.format("%.1f", balost) + "** <:diamond_ore:778675508069269504>\nMLC:  " + String.format("%.1f", mlc), false);
                                        emb.setFooter("MeltownEconomy");
                                        MessageEmbed memb = emb.build();
                                        e.getChannel().sendMessage(memb).queue();
                                    }
                                } else {
                                    if (transactions >= 1) {
                                        emb.setTitle("\uD83D\uDCB3" + " Кошелек " + mentionedMembers.get(0).getEffectiveName() + "#" + mentionedMembers.get(0).getUser().getDiscriminator());
                                        emb.addField("Баланс кошелька", "**" + stackbal + " стаков и " + String.format("%.1f", balost) + "** <:diamond_ore:778675508069269504>\nMLC:  " + String.format("%.1f", mlc), false);
                                        emb.addField("Последняя транзакция на сервере", timetransaction + " на сумму **" + transaction + "** <:diamond_ore:778675508069269504>", false);
                                        emb.setFooter("MeltownEconomy");
                                        MessageEmbed memb = emb.build();
                                        e.getChannel().sendMessage(memb).queue();
                                    } else {
                                        emb.setTitle("\uD83D\uDCB3" + " Кошелек " + mentionedMembers.get(0).getEffectiveName() + "#" + mentionedMembers.get(0).getUser().getDiscriminator());
                                        emb.addField("Баланс кошелька", "**" + stackbal + " стаков и " + String.format("%.1f", balost) + "** <:diamond_ore:778675508069269504>\nMLC:  " + String.format("%.1f", mlc), false);
                                        emb.setFooter("MeltownEconomy");
                                        MessageEmbed memb = emb.build();
                                        e.getChannel().sendMessage(memb).queue();
                                    }
                                }
                            } else {
                                if (transactions >= 1) {
                                    emb.setTitle("\uD83D\uDCB3" + " Кошелек " + mentionedMembers.get(0).getEffectiveName() + "#" + mentionedMembers.get(0).getUser().getDiscriminator());
                                    emb.addField("Баланс кошелька", "**" + bal + "** <:diamond_ore:778675508069269504>\nMLC:  " + String.format("%.1f", mlc), false);
                                    emb.addField("Последняя транзакция на сервере", timetransaction + " на сумму **" + transaction + "** <:diamond_ore:778675508069269504>", false);
                                    emb.setFooter("MeltownEconomy");
                                    MessageEmbed memb = emb.build();
                                    e.getChannel().sendMessage(memb).queue();
                                } else {
                                    emb.setTitle("\uD83D\uDCB3" + " Кошелек " + mentionedMembers.get(0).getEffectiveName() + "#" + mentionedMembers.get(0).getUser().getDiscriminator());
                                    emb.addField("Баланс кошелька", "**" + bal + "** <:diamond_ore:778675508069269504>\nMLC:  " + String.format("%.1f", mlc), false);
                                    emb.setFooter("MeltownEconomy");
                                    MessageEmbed memb = emb.build();
                                    e.getChannel().sendMessage(memb).queue();
                                }
                            }
                        } else {
                            String Query1 = "INSERT INTO EconomyDb(USERID, BAL, MELCOIN)" +
                                    "VALUES('" + mentioned + "' , '" + 0.1 + "' , '" + 0 + "')";
                            try {
                                connection = DriverManager.getConnection(DB_URL);
                                Statement st = connection.createStatement();
                                st.executeUpdate(Query1);
                                e.getChannel().sendMessage("Поздравляем! Это первый вход " + mentionedMembers.get(0).getAsMention() + " в виртуальный кошелек, удачных ему покупок)").queue();
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentRaw().startsWith("#baladd")) {
                try {
                    List<Member> mentionedMembers = e.getMessage().getMentionedMembers();
                    if (!mentionedMembers.isEmpty()) {
                        String[] messageSent = e.getMessage().getContentRaw().split(" ");
                        mentioned = mentionedMembers.get(0).getId();
                        connection = DriverManager.getConnection(DB_URL);
                        String query = "SELECT BAL FROM EconomyDb WHERE USERID = ?";
                        PreparedStatement prst = connection.prepareStatement(query);
                        prst.setString(1, mentioned);
                        ResultSet resultSet = prst.executeQuery();
                        while (resultSet.next()) {
                            bal = resultSet.getDouble("BAL");
                        }
                        double newbal = Integer.parseInt(messageSent[2]) + bal;
                        String Query = "UPDATE EconomyDb SET BAL = ? WHERE USERID = ?";
                        PreparedStatement prst2 = connection.prepareStatement(Query);
                        prst2.setDouble(1, newbal);
                        prst2.setString(2, mentioned);
                        prst2.execute();
                        e.getChannel().sendMessage("Баланс " + mentionedMembers.get(0).getAsMention() + " успешно пополнен на сумму " + messageSent[2] + " <:gold_ore:773869500948938752>").queue();
                    } else {
                        e.getChannel().sendMessage("Необходимо указать пользователя для пополнения!").queue();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentRaw().startsWith("#balset")) {
                try {
                    List<Member> mentionedMembers = e.getMessage().getMentionedMembers();
                    if (!mentionedMembers.isEmpty()) {
                        connection = DriverManager.getConnection(DB_URL);
                        String[] messageSent = e.getMessage().getContentRaw().split(" ");
                        mentioned = mentionedMembers.get(0).getId();
                        String query = "UPDATE EconomyDb SET BAL = ?" + " WHERE USERID = ?";
                        PreparedStatement prst = connection.prepareStatement(query);
                        prst.setDouble(1, Double.parseDouble(messageSent[2]));
                        prst.setString(2, mentioned);
                        prst.execute();
                        e.getChannel().sendMessage("Баланс " + mentionedMembers.get(0).getAsMention() + " успешно установлен на значение " + messageSent[2] + " <:gold_ore:773869500948938752> ").queue();
                    } else {
                        e.getChannel().sendMessage("Необходимо указать пользователя для установления!").queue();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentRaw().startsWith("#mlcset")) {
                List<Member> mentionedMembers = e.getMessage().getMentionedMembers();
                if (!mentionedMembers.isEmpty()) {
                    try {
                        connection = DriverManager.getConnection(DB_URL);
                        String[] messageSent = e.getMessage().getContentRaw().split(" ");
                        mentioned = mentionedMembers.get(0).getId();
                        String query = "UPDATE EconomyDb SET MELCOIN = ?" + " WHERE USERID = ?";
                        PreparedStatement prst = connection.prepareStatement(query);
                        prst.setDouble(1, Double.parseDouble(messageSent[2]));
                        prst.setString(2, mentioned);
                        prst.execute();
                        e.getChannel().sendMessage("Баланс мелкоинов " + mentionedMembers.get(0).getAsMention() + " успешно установлен на значение " + messageSent[2] + " MLC").queue();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                } else {
                    e.getChannel().sendMessage("Необходимо указать пользователя для установления!").queue();
                }
            }
        }
        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentRaw().startsWith("#pay")) {
                if (e.getMessage().getContentRaw().startsWith("#paymlc")) {
                    try {
                        mentionedMembers = e.getMessage().getMentionedMembers();
                        connection = DriverManager.getConnection(DB_URL);
                        String[] messageSent = e.getMessage().getContentRaw().split(" ");
                        mentioned = mentionedMembers.get(0).getId();
                        author = e.getAuthor().getId();
                        String query = "SELECT * FROM EconomyDb WHERE USERID = ?";
                        PreparedStatement prst = connection.prepareStatement(query);
                        prst.setString(1, author);
                        ResultSet resultSet = prst.executeQuery();
                        while (resultSet.next()) {
                            autholrmlc = resultSet.getDouble("MELCOIN");
                        }
                        b = Integer.parseInt(messageSent[2]);
                        if(autholrmlc > b){
                            String query1 = "SELECT MELCOIN FROM EconomyDb WHERE USERID = ?";
                            PreparedStatement prst1 = connection.prepareStatement(query1);
                            prst1.setString(1, mentioned);
                            ResultSet resultSet1 = prst1.executeQuery();
                            while (resultSet1.next()) {
                                bal = resultSet1.getDouble("MELCOIN");
                            }
                            double newbal = Integer.parseInt(messageSent[2]) + bal;
                            String Query = "UPDATE EconomyDb SET MELCOIN = ? "
                                    + " WHERE USERID = ?";
                            PreparedStatement prst2 = connection.prepareStatement(Query);
                            prst2.setDouble(1, newbal);
                            prst2.setString(2, mentioned);
                            prst2.execute();
                            //author
                            double newauthorbal = autholrmlc - Integer.parseInt(messageSent[2]);
                            String Query1 = "UPDATE EconomyDb SET MELCOIN = ?"
                                    + " WHERE USERID = ?";
                            PreparedStatement prst3 = connection.prepareStatement(Query1);
                            prst3.setDouble(1, newauthorbal);
                            prst3.setString(2, author);
                            prst3.execute();
                            e.getChannel().sendMessage("Пользователю " + mentionedMembers.get(0).getUser().getAsMention() + " переведено " + messageSent[2] + " MLC").queue();
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                } else if (e.getMessage().getContentRaw().startsWith("#payore")) {
                    mentionedMembers = e.getMessage().getMentionedMembers();
                    if (!mentionedMembers.isEmpty()) {
                        try {
                            connection = DriverManager.getConnection(DB_URL);
                            String[] messageSent = e.getMessage().getContentRaw().split(" ");
                            mentioned = mentionedMembers.get(0).getId();
                            author = e.getAuthor().getId();
                            String query = "SELECT * FROM EconomyDb WHERE USERID = ?";
                            PreparedStatement prst = connection.prepareStatement(query);
                            prst.setString(1, author);
                            ResultSet resultSet = prst.executeQuery();
                            while (resultSet.next()) {
                                authorbal = resultSet.getDouble("BAL");
                                autholrmlc = resultSet.getDouble("MELCOIN");
                            }
                            b = Integer.parseInt(messageSent[2]);
                            if (authorbal >= b) {
                                String query1 = "SELECT BAL FROM EconomyDb WHERE USERID = ?";
                                PreparedStatement prst1 = connection.prepareStatement(query1);
                                prst1.setString(1, mentioned);
                                ResultSet resultSet1 = prst1.executeQuery();
                                while (resultSet1.next()) {
                                    bal = resultSet1.getDouble("BAL");
                                }
                                double newbal = Integer.parseInt(messageSent[2]) + bal;
                                String Query = "UPDATE EconomyDb SET BAL = ? "
                                        + " WHERE USERID = ?";
                                PreparedStatement prst2 = connection.prepareStatement(Query);
                                prst2.setDouble(1, newbal);
                                prst2.setString(2, mentioned);
                                prst2.execute();
                                //author
                                double newauthorbal = authorbal - Integer.parseInt(messageSent[2]);
                                String Query1 = "UPDATE EconomyDb SET BAL = ?"
                                        + " WHERE USERID = ?";
                                PreparedStatement prst3 = connection.prepareStatement(Query1);
                                prst3.setDouble(1, newauthorbal);
                                prst3.setString(2, author);
                                prst3.execute();
                                e.getChannel().sendMessage("Пользователю " + mentionedMembers.get(0).getUser().getAsMention() + " переведено " + messageSent[2] + " <:gold_ore:773869500948938752>").queue();
                            } else {
                                connection = DriverManager.getConnection(DB_URL);
                                mentioned = mentionedMembers.get(0).getId();
                                String query1 = "SELECT * FROM EconomyDb WHERE USERID = ?";
                                PreparedStatement prst1 = connection.prepareStatement(query1);
                                prst1.setString(1, author);
                                ResultSet resultSet1 = prst.executeQuery();
                                while (resultSet1.next()) {
                                    authorbal = resultSet.getDouble("BAL");
                                    mlc = resultSet.getDouble("MELCOIN");
                                }
                                double toconvert = mlc * 30;
                                double newauthorbal = authorbal + toconvert;
                                double toSend = newauthorbal - b;
                                double mlcconvert = mlc - (toSend / 30);
                                msg = e.getChannel().sendMessage("На вашем ар-кошельке не хватает денег, не хотите ли вы конвертировать " + String.format("%.1f", mlcconvert) + " мелкоинов для оплаты?").complete();
                                msg.addReaction("\u2705").queue();
                                msg.addReaction("\u26D4").queue();
                                msgid = msg.getId();
                                msg.delete().queueAfter(1, TimeUnit.MINUTES);
                           }
                        } catch(SQLException throwables){
                            throwables.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e) {
        if (msgid != null) {
            if (e.getMessageId().equalsIgnoreCase(msgid)) {
                if(e.getMember().getId().equalsIgnoreCase(author)){
                    if(e.getReaction().getReactionEmote().getAsReactionCode().equalsIgnoreCase("\u2705")) {
                        try {
                            msg.clearReactions().queue();
                            msg.delete().queue();
                            connection = DriverManager.getConnection(DB_URL);
                            mentioned = mentionedMembers.get(0).getId();
                            String query = "SELECT * FROM EconomyDb WHERE USERID = ?";
                            PreparedStatement prst = connection.prepareStatement(query);
                            prst.setString(1, author);
                            ResultSet resultSet = prst.executeQuery();
                            while (resultSet.next()) {
                                authorbal = resultSet.getDouble("BAL");
                                mlc = resultSet.getDouble("MELCOIN");
                            }
                            double toconvert = mlc * 30;
                            double newauthorbal = authorbal + toconvert;
                            if (newauthorbal > b) {
                                double toSend = newauthorbal - b;
                                double mlcconvert = toSend / 30;
                                //melcoins
                                String sendQuery = "UPDATE EconomyDb SET MELCOIN = ? WHERE USERID = ?";
                                PreparedStatement prst2 = connection.prepareStatement(sendQuery);
                                prst2.setDouble(1, mlcconvert);
                                prst2.setString(2, author);
                                prst2.execute();
                                //authorbal
                                String sendBalQuery = "UPDATE EconomyDb SET BAL = ? WHERE USERID = ?";
                                PreparedStatement prst4 = connection.prepareStatement(sendBalQuery);
                                prst4.setDouble(1, 0);
                                prst4.setString(2, author);
                                prst4.execute();
                                //receiverbal
                                String query1 = "SELECT BAL FROM EconomyDb WHERE USERID = ?";
                                PreparedStatement prst1 = connection.prepareStatement(query1);
                                prst1.setString(1, mentioned);
                                ResultSet resultSet1 = prst1.executeQuery();
                                while (resultSet1.next()) {
                                    bal = resultSet1.getDouble("BAL");
                                }
                                String Query = "UPDATE EconomyDb SET BAL = ? "
                                        + " WHERE USERID = ?";
                                double newbal = bal + b;
                                PreparedStatement prst3 = connection.prepareStatement(Query);
                                prst3.setDouble(1, newbal);
                                prst3.setString(2, mentioned);
                                prst3.execute();
                                e.getChannel().sendMessage("Пользователю " + mentionedMembers.get(0).getUser().getAsMention() + " переведено " + b + " <:gold_ore:773869500948938752>").queue();
                            } else if (newauthorbal == b) {
                                String query1 = "SELECT BAL FROM EconomyDb WHERE USERID = ?";
                                PreparedStatement prst1 = connection.prepareStatement(query1);
                                prst1.setString(1, mentioned);
                                ResultSet resultSet1 = prst1.executeQuery();
                                while (resultSet1.next()) {
                                    bal = resultSet1.getInt("BAL");
                                }
                                double newbal = newauthorbal + bal;
                                String Query = "UPDATE EconomyDb SET BAL = ? "
                                        + " WHERE USERID = ?";
                                PreparedStatement prst2 = connection.prepareStatement(Query);
                                prst2.setDouble(1, newbal);
                                prst2.setString(2, mentioned);
                                prst2.execute();
                            } else {
                                e.getChannel().sendMessage("У вас недостаточно денег на счету!").queue();
                            }
                        }catch(SQLException e1){
                            e1.printStackTrace();
                        }
                    } else if(e.getReaction().getReactionEmote().getAsReactionCode().equalsIgnoreCase("\u26D4")){
                        msg.clearReactions().queue();
                        msg.delete().queue();
                    }
                }
            }
        }
    }
}
