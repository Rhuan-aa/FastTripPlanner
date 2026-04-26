package br.edu.ifsp.scl.sc3043983.fasttripplanner.model

enum class HostingType(val description: String) {
    ECONOMIC("Econômica"),
    CONFORT("Conforto"),
    DELUXE("Luxo");

    override fun toString(): String {
        return description
    }


}