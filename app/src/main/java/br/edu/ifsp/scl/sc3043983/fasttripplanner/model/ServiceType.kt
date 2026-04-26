package br.edu.ifsp.scl.sc3043983.fasttripplanner.model

enum class ServiceType(val description: String, val price : Int) {
    TRANSPORT("Transporte", 300),
    FOOD("Alimentação", 50),
    TOURS("Turismo", 120);

    override fun toString(): String {
        return description
    }

}