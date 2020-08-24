object DFS{
    def makeAdjLists(g: Group, gens: Array[Element]): Array[(Element, Array[Element])] = {
        val elements = g.getElements()
        val cayleyTable = g.getTable()

        val adjLists: Array[(Element, Array[Element])] = new Array(elements.size)

        var i = 0
        for(e <- elements){
            val adj = new Array[Element](gens.size)
            
            var j = 0
            for(gen <- gens){
                adj(j) = g.multiply(e, gen)

                j += 1
            }

            adjLists(i) = (e, adj)
            i += 1
        }

        return adjLists
    }

    def dfs(elements: Array[Element], adjLists: Array[(Element, Array[Element])]): List[State] = {
        val dfsElements: Array[DFSElement] = elements.map(new DFSElement(_, new Status))

        def findElement(e: Element): Int = {
            var i = 0
            while(dfsElements(i).getElement() != e){
                i += 1
            }

            return i
        }

        def findAdjList(e: Element): Int = {
            var i = 0
            while(adjLists(i)._1 != e){ 
                i += 1
            }

            return i
        }

        var time = 0
        var states: List[State] = List()

        def makeNewState(e: Element, currentDFS: Array[DFSElement]): State = {
            return new State(e, currentDFS)
        }

        def dfsVisit(e: Element): Unit = {
            time += 1

            val index = findElement(e)

            states = states :+ new State(e, dfsElements)

            dfsElements(index).makeGrey()

            val adjList = adjLists(findAdjList(e))._2
            for(v <- adjList){
                if(dfsElements(findElement(v)).getStatus() == "white"){
                    dfsVisit(v)
                    states = states :+ new State(e, dfsElements)
                }
            }

            time += 1
            dfsElements(index).makeBlack()
        }

        dfsVisit(elements(0))
        
        states = states :+ new State(null, dfsElements)

        return states
    }
}

class Status{
    private var status = "white"
    def getStatus(): String = status

    def makeGrey(): Unit = {
        status = "grey"
    }

    def makeBlack(): Unit = {
        status = "black"
    }
}

class DFSElement(e: Element, status: Status){
    private var element = e
    def getElement(): Element = e
    
    private var currentStatus = status
    def getStatus(): String = status.getStatus()

    def makeGrey(): Unit = {
        currentStatus.makeGrey()
    }

    def makeBlack(): Unit = {
        currentStatus.makeBlack()
    }
}

class State(activeElement: Element, dfsInfo: Array[DFSElement]){
    def getActiveElement() = activeElement

    private val status = dfsInfo.map((e: DFSElement) => new DFSElement(e.getElement(), State.statusToObject(e.getStatus())))
    def getDFSInfo() = status

    override def toString(): String = {
        var output = "Active Element: "

        output += activeElement.toString + "; "

        for(e <- getDFSInfo()){
            output += e.getElement().toString + ": " + e.getStatus + ", "
        }

        return output
    }
}

object State{
    def statusToObject(s: String): Status = {
        val obj = new Status

        if(s == "grey"){
            obj.makeGrey()
        } else if(s == "black"){
            obj.makeBlack()
        }

        return obj
    }
}