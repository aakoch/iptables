package com.adamkoch

class Cell {
    var currentValue = false
    val connections = mutableListOf<Cell>()

    fun calculateNextValue() {
        val total : Int = connections.sumBy {
            if (it.currentValue) {
                1
            } else {
                0
            }
        }

        if (currentValue) {
            if (total < 2)
                currentValue = false
            else if (total < 4)
                currentValue = true
            else
                currentValue = false
        }
        else if (total == 3)
            currentValue = true;
    }
}