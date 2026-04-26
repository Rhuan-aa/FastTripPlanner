package br.edu.ifsp.scl.sc3043983.fasttripplanner.model

enum class ServiceType(val description: String) {
    TRANSPORT("Transporte"),
    FOOD("Alimentação"),
    TOURS("Turismo");

    override fun toString(): String {
        return description
    }

}