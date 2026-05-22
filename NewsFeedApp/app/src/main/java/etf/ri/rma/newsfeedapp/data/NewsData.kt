package etf.ri.rma.newsfeedapp.data

import etf.ri.rma.newsfeedapp.model.NewsItem

object NewsData {
    fun getAllNews(): List<NewsItem> {
        return listOf(
            NewsItem("0", "Velika politička odluka donijeta danas", "Danas je u Parlamentu donesena nova odluka koja će uticati na budućnost zemlje...", "https://interview.ba/wp-content/uploads/2024/09/fikret-1024x652.png", "politics", false, "Rtrs", "01-04-2024", arrayListOf()),
            NewsItem("1", "Nevjerovatan preokret na utakmici", "Ekipa se vratila iz ogromnog zaostatka i pobjedila u posljednjim minutama...", "https://scsport.ba/wp-content/uploads/2023/02/verona566-860x464.jpg", "sports", true, "Sport klub", "02-04-2024", arrayListOf()),
            NewsItem("2", "Novi izum u tehnologiji", "Istraživači su razvili novu metodu koja bi mogla promijeniti industriju...", "https://avaz.ba/storage/images/2024/04/12/jetzero.jpg", "tech", false, "Avaz", "03-04-2024", arrayListOf()),
            NewsItem("3", "Svjetski lideri sastali se u Parizu", "Na samitu u Parizu diskutovali su o globalnim pitanjima, uključujući ekonomiju i klimatske promjene...", "https://i.imgur.com/T0b4d4M.jpg", "politics", true, "Bbc", "04-04-2024", arrayListOf()),
            NewsItem("4", "Fantastičan gol na evropskom prvenstvu", "Najveća senzacija na utakmici bila je fantastičan gol iz sredine terena...", "https://sportklub.n1info.hr/wp-content/uploads/2025/03/16/1742162964-25075796533595-750x500.jpg", "sports", false, "N1", "05-04-2024", arrayListOf()),
            NewsItem("5", "Nova tehnologija u medicini", "Novi uređaj može revolucionirati način na koji se tretiraju bolesti srca...", "https://balkans.aljazeera.net/wp-content/uploads/2024/09/ai-medicine.jpg", "health", true, "Al jazeera", "06-04-2024", arrayListOf()),
            NewsItem("6", "Predsjednik izjavio o novim reformama", "Predsjednik je najavio nove reforme u oblasti obrazovanja i zdravstva...", "https://federalna.ba/storage/images/2025/05/24/becirovic-radev.jpg", "politics", false, "Politika.ba", "07-04-2024", arrayListOf()),
            NewsItem("7", "Sportski rekord oboren na Svjetskom prvenstvu", "Rekord je oboren u poslednjem trenutku finalne utrke, na veliko iznenađenje publike...", "https://sportske.jutarnji.hr/image/15362535/620/0.0/0.0/0/2023/07/20/REUTERS.jpg", "sports", true, "Sport1", "08-04-2024", arrayListOf()),
            NewsItem("8", "Napredak u istraživanjima svemira", "NASA je otkrila nove informacije o strukturi svemira koje bi mogle da promijene naše razumijevanje...", "https://i.imgur.com/eB0r07L.jpg", "science", false, "Space news", "31-03-2024", arrayListOf()),
            NewsItem("9", "Započeta izgradnja novog stadiona", "Stadion će biti najmoderniji u regionu, a gradnja bi trebala biti završena do kraja godine...", "https://i.imgur.com/x0T2H3m.jpg", "sports", false, "Arena sport", "30-03-2024", arrayListOf()),
            NewsItem("10", "Izbori u evropi: Šta slijedi?", "Evropski izbori donose nove političke talase i mogu značajno uticati na politiku u regiji...", "https://i.imgur.com/1G6y4N0.jpg", "politics", true, "Euronews", "29-03-2024", arrayListOf()),
            NewsItem("11", "Revolucija u automobilskoj industriji", "Nova tehnologija u automobilskoj industriji mogla bi smanjiti emisije i potrošnju goriva...", "https://i.imgur.com/2sP8k5H.jpg", "tech", false, "Auto world", "28-03-2024", arrayListOf()),
            NewsItem("12", "Nogometna reprezentacija nastavlja put ka Mundijalu", "Nacionalna reprezentacija je u odličnoj formi i vodi na tabeli kvalifikacija...", "https://i.imgur.com/uR2T8nJ.jpg", "sports", true, "Fudbal.ba", "27-03-2024", arrayListOf()),
            NewsItem("13", "Svjetski lideri najavili nove klimatske mjere", "Na svjetskom samitu objavljene su nove mjere za borbu protiv klimatskih promjena...", "https://i.imgur.com/m9K0X9Q.jpg", "politics", false, "Reuters", "26-03-2024", arrayListOf()),
            NewsItem("14", "Izum koji bi mogao spasiti hiljade života", "Novi izum u oblasti medicine omogućava brže dijagnostikovanje bolesti...", "https://i.imgur.com/9C0F5tM.jpg", "health", true, "Techcrunch", "25-03-2024", arrayListOf()),
            NewsItem("15", "Sportski turnir u zagrebu", "U Zagrebu se održava međunarodni sportski turnir sa velikim brojem učesnika...", "https://i.imgur.com/bW3K5fH.jpg", "sports", false, "Eurosport", "24-03-2024", arrayListOf()),
            NewsItem("16", "Preporuke za nove ekonomske reforme", "Ekonomski stručnjaci preporučuju dalja ulaganja u infrastrukturu i obrazovni sistem...", "https://i.imgur.com/A6D5Q0G.jpg", "business", true, "The economist", "23-03-2024", arrayListOf()),
            NewsItem("17", "Izvanredno istraživanje u oblasti robotike", "Robotika je napravila ogroman napredak u sposobnostima interakcije s ljudima...", "https://i.imgur.com/n1L7Z2Y.jpg", "tech", false, "Tech radar", "22-03-2024", arrayListOf()),
            NewsItem("18", "Predstojeći veliki sportski događaj", "Veliki sportski događaj će okupiti najbolje sportiste iz cijelog svijeta...", "https://i.imgur.com/C0X5M7B.jpg", "sports", true, "Sport24", "21-03-2024", arrayListOf()),
            NewsItem("19", "Politika i ekologija: važan samit", "Lideri svijeta će se okupiti da donesu nove ekološke politike za zaštitu planete...", "https://i.imgur.com/X8B3G2S.jpg", "politics", false, "Global news", "20-03-2024", arrayListOf())
        )
    }
}
