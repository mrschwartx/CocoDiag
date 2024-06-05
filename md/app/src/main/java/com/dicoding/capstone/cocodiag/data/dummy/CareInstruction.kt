package com.dicoding.capstone.cocodiag.data.dummy

data class CareInstruction(
    val diseaseName: String,
    val identification: String,
    val treatment: String
)

val dummyCareInstructions = listOf(
    CareInstruction(
        "Busuk Pangkal Batang (Basal Stem Rot)",
        "Identifikasi: Tanda-tanda awal meliputi pembusukan daun, pengelupasan batang, dan kehilangan turgor pada kelapa.",
        "Perawatan:\n" +
                "- Potong dan buang bagian yang terinfeksi batang dengan hati-hati untuk mencegah penyebaran penyakit.\n" +
                "- Berikan pupuk organik untuk meningkatkan kekebalan tanaman.\n" +
                "- Hindari penyiraman berlebihan yang dapat menciptakan lingkungan yang mendukung pertumbuhan jamur."
    ),
    CareInstruction(
        "Bercak Daun (Leaf Spot)",
        "Identifikasi: Bercak coklat atau hitam pada daun kelapa, disertai dengan penurunan pertumbuhan tanaman.",
        "Perawatan:\n" +
                "- Potong dan buang daun yang terinfeksi untuk mengurangi penyebaran penyakit.\n" +
                "- Aplikasikan fungisida yang direkomendasikan secara teratur untuk mengendalikan infeksi.\n" +
                "- Pastikan penyiraman tanaman dilakukan pada pagi hari dan hindari membasahi daun saat penyiraman."
    ),
    CareInstruction(
        "Layu Bakteri (Bacterial Wilt)",
        "Identifikasi: Daun tanaman kelapa mulai menguning, layu, dan akhirnya mati. Terkadang terlihat cairan lendir berwarna putih atau kekuningan pada batang.",
        "Perawatan:\n" +
                "- Potong dan buang tanaman yang terinfeksi secara menyeluruh untuk mencegah penyebaran lebih lanjut.\n" +
                "- Tanam varietas kelapa yang lebih tahan terhadap layu bakteri jika tersedia.\n" +
                "- Gunakan sistem drainase yang baik untuk mengurangi kelembaban tanah dan menghambat pertumbuhan bakteri."
    ),
    CareInstruction(
        "Penyakit Mahkota Tanah (Crown Rot)",
        "Identifikasi: Daun bagian atas tanaman kelapa menguning, mengering, dan rontok. Pada tahap lanjut, mahkota tanaman akan membusuk.",
        "Perawatan:\n" +
                "- Periksa sistem drainase dan pastikan tanah tidak tergenang air.\n" +
                "- Potong dan buang daun yang terinfeksi, kemudian aplikasikan fungisida pada bagian yang terkena.\n" +
                "- Berikan pupuk yang seimbang untuk meningkatkan daya tahan tanaman terhadap penyakit."
    ),
    CareInstruction(
        "Penyakit Embun Tepung (Powdery Mildew)",
        "Identifikasi: Permukaan daun kelapa ditutupi oleh lapisan serbuk putih yang terlihat seperti tepung.",
        "Perawatan:\n" +
                "- Aplikasikan fungisida yang sesuai secara teratur sesuai dengan instruksi label.\n" +
                "- Tanam kelapa di lokasi yang memiliki sirkulasi udara yang baik untuk mengurangi kelembaban.\n" +
                "- Jaga kebersihan kebun dengan membuang daun dan cabang yang jatuh."
    )
)
