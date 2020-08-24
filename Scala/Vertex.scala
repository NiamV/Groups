class Vertex(e: Element, xCoord: Int, yCoord: Int, selectedInput: Boolean, statusInput: Status, active: Boolean){
    def element: Element = e
    def name: String = e.name

    private var currentx = xCoord
    def x: Int = currentx
    def updatex(newx: Int): Unit = {
        currentx = newx
    }
    
    private var currenty = yCoord
    def y: Int = currenty
    def updatey(newy: Int): Unit = {
        currenty = newy
    }

    private var isSelected = selectedInput
    def selected(): Boolean = isSelected
    def updateSelected(): Unit = {
        isSelected = !isSelected
    }

    private var currentStatus = statusInput
    def status(): Status = currentStatus
    def updateStatus(newStatus: Status): Unit = {
        currentStatus = newStatus
    }

    private var currentActive = active
    def active(): Boolean = currentActive
    def updateActive(newActive: Boolean) = {
        currentActive = newActive
    }
}