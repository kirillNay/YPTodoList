package nay.kirill.ypracticum.todo.domain

data class Task(
    val id: ID = ID.Undefined,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false
)

@JvmInline
value class ID(val value: Int) {

    companion object {
        val Undefined = ID(-1)
    }

}