package com.corbandalas.shelterbot.telegram.menu;

public class ShelterBotTexts {

    public static final String MAIN_MENU_OPTION1 = "\uD83D\uDCA3 Список убежищ";
    public static final String MAIN_MENU_OPTION2 = "\uD83D\uDCE1 Убежища рядом";
    public static final String CHOOSE_CAPTION =  ", выберите действие";
    public static final String CHOOSE_CITY_CAPTION =  ", выберите город";
    public static final String CHOOSE_DISTRICT_CAPTION =  ", выберите район";
    public static final String CHOOSE_SHELTER_CAPTION =  ", выберите убежище";
    public static final String BACK =  "\uD83D\uDD19 Назад";
    public static final String ASK_FOR_GPS_CAPTION =  "Координаты";
    public static final String GOODBYE_CAPTION =  ", до свидания!";
    public static final String BACK_MAIN_MANU =  "Главное меню";
    public static final String NEXT_PAGE =  "➡️ Следующая страница";
    public static final String PREV_PAGE =  "⬅️ Предыдущая страница";
    public static final String NOT_SHELTERS_AROUND =  "Поблизости с вами нет убежищ";
    public static final String SHOW_ON_MAP =  "\uD83D\uDDFA️ Показать на карте";
    public static final String ABOUT_TEXT =  "@shelter_bomb_bot v1.0.0 \n\nЧат-бот для показа бомбоубежищ Донецкой Народной Республики.\n\n" +
            "В работе приложения использованы официальные списки бомбоубежищ от 19.02.2022 с ресурса https://vsednr.ru/adresa-bomboubezhishh-v-donecke/\n\n" +
            "База бомбоубежищ населенных пунктов ДНР находится в стадии наполнения. Благодарим за терпение. Увы, но списки непригодны для автоматического парсинга, необходима кропотливая ручная работа.\n\n" +
            "Приложение работает на условно-бесплатном serverless движке,поэтому возможны \"залипания\" приложения на 15 секунд при простое. Отнеситесь к этому с пониманием.\n\n" +
            "Обратная связь: https://vk.com/shelter_bomb_bot \n\n" +
            "Победа будет за нами! Сила V правде!";
    public static final String HELP_TEXT =  "@shelter_bomb_bot v1.0.0 \n\n" +
            "Наш чат-бот предлагает два основных сервиса:\n\n" +
            "-База данных бомбоубежищ, структурированная по городам и районам населенных пунктов ДНР.\n" +
            "-Функция поиска ближайших бомбоубежищ по геолокации вашего телефона\n\n" +
            "Для поиска нужного бомбоубежища выберите в главном меню пункт \"Список убежищ\", затем - ваш населенный пункт(со временем будут добавлены другие города и поселки). " +
            "Затем выберите небходимый район и листайте список бомбоубежищ, включающиий адрес, кол-во мест, а также указание где брать ключи. \n\n" +

            "Для поиска ближайших бомбоубежищ выберите в главном меню пункт \"Убежища рядом\", подтвердите на телефоне предоставление ваших GPS-координат. " +
            "Через некоторое время будет показан нумерованный список ближайших убежищ с адресами. Вы можете выбрать опцию \"Показать на карте\" и посмотреть примерное расположение убежищ на карте," +
            " центрированной по вашему текущему местоположению";

}
