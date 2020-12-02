import java.io.File

fun readInputForDay(day: Int): String {
    return File("res/input$day.txt").readText()
}