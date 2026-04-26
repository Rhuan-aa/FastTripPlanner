package br.edu.ifsp.scl.sc3043983.fasttripplanner.model

enum class HostingType(val description: String, val modifier: Double) {
    ECONOMIC("Econômica", 1.0),
    CONFORT("Conforto", 1.5),
    DELUXE("Luxo", 2.2);

    override fun toString(): String {
        return description
    }


}