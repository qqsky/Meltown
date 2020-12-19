package meltown;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Date;

public class Help extends ListenerAdapter {
    private static final String helpsyntax = "#help";

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentRaw().equalsIgnoreCase("#help")) {
                EmbedBuilder helpEb = new EmbedBuilder();
                helpEb.setAuthor(e.getAuthor().getName(), e.getAuthor().getAvatarUrl(), e.getAuthor().getAvatarUrl());
                helpEb.setTitle("Help");
                helpEb.setDescription("`help mod`\n`help utils`\n`help economy`");
                helpEb.setFooter("qqsky Dev");
                helpEb.setTimestamp(new Date().toInstant());
                MessageEmbed helpmessage = helpEb.build();
                e.getChannel().sendMessage(helpmessage).queue();
            }
        }
        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentRaw().startsWith("#help")) {
                String messageSent = e.getMessage().getContentRaw().substring(helpsyntax.length());
                switch (messageSent) {
                    case " mod":
                        EmbedBuilder modhelp = new EmbedBuilder();
                        modhelp.setTitle("Mod help");
                        modhelp.setDescription("[] - *обязательный аргумент*\n() - *необязательный аргумент*");
                        modhelp.setFooter("Команду вызвал " + e.getAuthor().getName(), e.getJDA().getSelfUser().getAvatarUrl());
                        modhelp.setColor(Color.ORANGE);
                        modhelp.setTimestamp(new Date().toInstant());
                        MessageEmbed.Field unmuteField = new MessageEmbed.Field("Unmute", "Использование: `#unmute [@user]`\n```Размучивает указанного пользователя```", false);
                        MessageEmbed.Field muteField = new MessageEmbed.Field("Mute", "Использование: `#mute [@user] (причина)`\n```Затыкает указанного пользователя```", false);
                        MessageEmbed.Field tempmuteField = new MessageEmbed.Field("Tempmute", "Использование: `#tempmute [@user] [время] (пример: 1(d, h, s)) (причина)`\n```Затыкает пользователя на указанное время в команде```", false);
                        MessageEmbed.Field setupField = new MessageEmbed.Field("Setup", "Использование: `#setup [@role]`\n```Setup-команда для модерации```", false);
                        modhelp.addField(setupField);
                        modhelp.addField(muteField);
                        modhelp.addField(unmuteField);
                        modhelp.addField(tempmuteField);
                        MessageEmbed modmessagehelp = modhelp.build();
                        e.getChannel().sendMessage(modmessagehelp).queue();
                        break;
                    case " utils":
                        EmbedBuilder utilhelp = new EmbedBuilder();
                        utilhelp.setTitle("Utils help");
                        utilhelp.setDescription("[] - *обязательный аргумент*\n() - *необязательный аргумент*");
                        utilhelp.setFooter("Команду вызвал " + e.getAuthor().getName(), e.getJDA().getSelfUser().getAvatarUrl());
                        utilhelp.setColor(Color.ORANGE);
                        utilhelp.setTimestamp(new Date().toInstant());
                        MessageEmbed.Field sayField = new MessageEmbed.Field("Say", "Использование: `#say [сообщение]`\n```Сказать что-либо от лица бота```", false);
                        MessageEmbed.Field typeField = new MessageEmbed.Field("Type", "Использование: `#type [сообщение]`\n```Данная команда позволяет сказать что-либо от лица бота с эффектом клавиатурного набора```", false);
                        MessageEmbed.Field pingField = new MessageEmbed.Field("Stat", "Использование: `#stat`\n```Статистика бота```", false);
                        MessageEmbed.Field emoteField = new MessageEmbed.Field("Emote", "Использование: `#emote [любое дискорд-емодзи]`\n```Команда позволяет конвертировать емодзи в картинку```", false);
                        MessageEmbed.Field sinfofield = new MessageEmbed.Field("S-info", "Использование: `#s-info`\n```Основная информация о сервере```", false);
                        MessageEmbed.Field giveawayfield = new MessageEmbed.Field("Giveaway", "Использование: `#gw, #giveway [время] (пример: 1(d, h, s)) [приз]`\n```Создает розыгрыш с указанными вами призом```", false);
                        MessageEmbed.Field rerollfield = new MessageEmbed.Field("Reroll", "Использование: `#reroll, #r [айди сообщения]`\n```Перевыбирает победителя розыгрыша```", false);
                        utilhelp.addField(pingField);
                        utilhelp.addField(sayField);
                        utilhelp.addField(typeField);
                        utilhelp.addField(emoteField);
                        utilhelp.addField(sinfofield);
                        utilhelp.addField(giveawayfield);
                        utilhelp.addField(rerollfield);
                        MessageEmbed utilmessagehelp = utilhelp.build();
                        e.getChannel().sendMessage(utilmessagehelp).queue();
                        break;
                    case " economy":
                        EmbedBuilder economyHelp = new EmbedBuilder();
                        economyHelp.setTitle("Economy help");
                        economyHelp.setDescription("[] - *обязательный аргумент*\n() - *необязательный аргумент*");
                        economyHelp.setFooter("Команду вызвал " + e.getAuthor().getName(), e.getJDA().getSelfUser().getAvatarUrl());
                        economyHelp.setColor(Color.ORANGE);
                        economyHelp.setTimestamp(new Date().toInstant());
                        MessageEmbed.Field regField = new MessageEmbed.Field("Reg", "Использование: `#reg`\n```Регистирует вас в виртуальном кошельке```", false);
                        MessageEmbed.Field moneyField = new MessageEmbed.Field("Money", "Использование: `#money (@user)`\n```Показывает ваш баланс или баланс указанного вами пользователя```", false);
                        MessageEmbed.Field addField = new MessageEmbed.Field("Add", "Использование: `#add [@user] [сумма]`\n`Команда доступна только банкирам`\n```Добавляет указанную в сообщении сумму на баланс указанного вами пользователя```", false);
                        MessageEmbed.Field setField = new MessageEmbed.Field("Set", "Использование: `#set [@user] [сумма]`\n`Команда доступна только банкирам`\n```Устанавливает баланс указанного вами пользователя на  указанную в сообщении сумму```", false);
                        MessageEmbed.Field payField = new MessageEmbed.Field("Pay", "Использование: `#pay [@user] [сумма]`\n```Переводит указанную сумму на баланс упомянутого в сообщении пользователя```", false);
                        MessageEmbed.Field withField = new MessageEmbed.Field("With", "Использование: `#with [сумма]`\n```Отправляет заказ на вывод золотой руды из виртуального кошелька в наличные деньги```", false);
                        economyHelp.addField(regField);
                        economyHelp.addField(moneyField);
                        economyHelp.addField(addField);
                        economyHelp.addField(setField);
                        economyHelp.addField(payField);
                        economyHelp.addField(withField);
                        MessageEmbed memb = economyHelp.build();
                        e.getChannel().sendMessage(memb).queue();
                }
            }
        }
    }
}
