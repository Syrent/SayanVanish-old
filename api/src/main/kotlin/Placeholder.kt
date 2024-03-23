package ir.syrent

interface Placeholder {

    val from: String
    val to: String

    companion object {
        fun parsed(key: String, context: String): String {
        }
    }
}