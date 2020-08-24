import scala.swing._
import scala.swing.event._
import java.awt.Color

class Graph(group: Group) extends SimpleSwingApplication{
    def top = new MainFrame{
        val elements = group.getElements()
        val order = elements.size

        var generators = group.getGenerators()
        val colours = Array(
            new Color(252, 53, 3),
            new Color(3, 69, 252),
            new Color(21, 209, 102),
            new Color(156, 21, 209)
        )
        
        private def getCoords(total: Int, current: Int): (Int, Int) = {
            val angle = 2 * math.Pi * current / total

            val x = (300 * math.sin(angle)).toInt
            val y = (300 * math.cos(angle)).toInt

            return (450 + x, 450 - y)
        }

        val verticies = new Array[Vertex](order)
        for(i <- 0 until order){
            val coords = getCoords(order, i)
            verticies(i) = new Vertex(elements(i), coords._1, coords._2, false, new Status, false)
        }

        private def findVertex(e: Element): Int = {
            var index = 0
            while(verticies(index).element != e){
                index += 1
            }

            return index
        }

        val ct = group.getTable()

        private def overVertex(x: Int, y: Int): Int = {
            var result = -1

            var i = 0
            for(v <- verticies){
                if(math.abs(v.x - x) < 20 && math.abs(v.y - y) < 20){
                    result = i
                }

                i += 1
            }

            return result
        }

        var showingDFS = false

        private def dfsDialogue(b: Boolean) = {
            var message = ""

            if(b){
                message = "This set generates the group"
            } else{
                message = "This set does not generate the group"
            }

            Dialog.showMessage(contents.head, message)
        }

        val graphGraphic = new Component{
            override def paintComponent(g: Graphics2D){
                super.paintComponent(g)

                g.setColor(new Color(255, 255, 255))
                g.fillRect(0, 0, 1000, 900)
                g.setColor(new Color(0, 0, 0))

                def drawArrow(p: Element, q: Element) = {
                    val startVertex = verticies(findVertex(p))
                    val endVertex = verticies(findVertex(q))

                    val sx = startVertex.x
                    val sy = startVertex.y
                    val ex = endVertex.x
                    val ey = endVertex.y

                    val vector = new Vector(sx, sy, ex, ey)

                    val floatingVector = new FloatingVector(ex - sx, ey - sy)
                    val shiftStart = floatingVector.scale(40)
                    val shiftEnd = floatingVector.scale(-40)

                    val arrowVector = vector.moveStart(shiftStart).moveEnd(shiftEnd)

                    g.drawLine(arrowVector.startx, arrowVector.starty, arrowVector.endx, arrowVector.endy)

                    val headVectorLeft = floatingVector.scale(20).rotate(5 * math.Pi / 6)
                    val headVectorRight = floatingVector.scale(20).rotate(-5 * math.Pi / 6)

                    val leftEndPoint = arrowVector.moveEnd(headVectorLeft)
                    val rightEndPoint = arrowVector.moveEnd(headVectorRight)

                    val xPoints = Array(arrowVector.endx, leftEndPoint.endx, rightEndPoint.endx)
                    val yPoints = Array(arrowVector.endy, leftEndPoint.endy, rightEndPoint.endy)

                    g.fillPolygon(xPoints, yPoints, 3)
                }

                if(showingDFS){
                    for(v <- verticies){
                        def statusToColour(s: String): Color = {
                            var colour = new Color(0,0,0)
                            if(s == "white"){
                                colour = new Color(207, 33, 21)
                            } else if(s == "grey"){
                                colour = new Color(209, 168, 21)
                            } else {
                                colour = new Color(16, 125, 62)
                            }

                            return colour
                        }

                        g.setColor(statusToColour(v.status().getStatus()))
                        if(v.active() == true){
                            g.setColor(new Color(33, 21, 209))
                        }

                        val fontMetrics = g.getFontMetrics()
                        val strCoordx = v.x - (fontMetrics.stringWidth(v.name) / 2)
                        val strCoordy = v.y + (fontMetrics.getHeight() / 4)
                
                        g.fillOval(v.x-25, v.y-25, 50, 50)
                        
                        g.setColor(new Color(255, 255, 255))
                        g.drawString(v.name, strCoordx, strCoordy)

                        g.setColor(new Color(0, 0, 0))
                    }

                    for(gen <- generators){
                        for(h <- elements){
                            drawArrow(h, group.multiply(h, gen))
                        }
                    }
                }
                else{
                    for(v <- verticies){
                        if(v.selected() == true){
                            g.setColor(new Color(0, 255, 0))
                        }

                        val fontMetrics = g.getFontMetrics()
                        val strCoordx = v.x - (fontMetrics.stringWidth(v.name) / 2)
                        val strCoordy = v.y + (fontMetrics.getHeight() / 4)
                
                        g.drawString(v.name, strCoordx, strCoordy)
                        g.drawOval(v.x-25, v.y-25, 50, 50)

                        g.setColor(new Color(0, 0, 0))
                    }

                    var i = 0
                    for(gen <- generators){
                        g.setColor(colours(i))
                        for(h <- elements){
                            drawArrow(h, group.multiply(h, gen))
                        }
                        i += 1
                    }
                }
            }

            listenTo(mouse.clicks)
            listenTo(mouse.moves)
            
            reactions += {
                case e: MouseClicked => {
                    val selectedVertex = overVertex(e.point.x, e.point.y)
                    if(selectedVertex != -1){
                        verticies(selectedVertex).updateSelected()
                    }

                    repaint()
                }
                case e: MouseDragged => {
                    val selectedVertex = overVertex(e.point.x, e.point.y)

                    if(selectedVertex != -1){
                        verticies(selectedVertex).updatex(e.point.x)
                        verticies(selectedVertex).updatey(e.point.y)
                    }

                    repaint()
                }
            }

            border = new javax.swing.border.LineBorder(new Color(0,0,0))
            minimumSize = new Dimension(1000, 900)
            preferredSize = minimumSize
            maximumSize = minimumSize
        }

        val controls: FlowPanel = new FlowPanel{
            val updateGensButton = new Button("Update Generators")
            contents += updateGensButton

            val checkGeneratesButton = new Button("Check if Generating")
            contents += checkGeneratesButton

            listenTo(updateGensButton)
            listenTo(checkGeneratesButton)
            
            reactions += {
                case ButtonClicked(b) => {
                    if(b == updateGensButton){
                        var newGenerators: List[Element] = List()
                        for(v <- verticies){
                            if(v.selected()){
                                newGenerators = v.element :: newGenerators
                                v.updateSelected()
                            }                    
                        }

                        if(newGenerators.size < 5){
                            generators = newGenerators.toArray.reverse
                        }
                        
                        graphGraphic.repaint()

                        generatorsLabel.contents.clear()
                        generatorsLabel.contents += new Label("Generators:"){
                            font = new Font("Dialog", java.awt.Font.BOLD, 18)
                        }
            
                        var i = 0
                        for(g <- generators){
                            generatorsLabel.contents += new Label("    " + g.toString){
                                foreground = colours(i)
                                font = new Font("Dialog", java.awt.Font.BOLD, 18)
                            }
                            i += 1
                        }

                        generatorsLabel.revalidate()
                        generatorsLabel.repaint()
                    }

                    if(b == checkGeneratesButton){
                        val adjLists = DFS.makeAdjLists(group, generators)
                        val results = DFS.dfs(group.getElements(), adjLists)

                        showingDFS = true

                        val runDFS = new Thread{
                            override def run{
                                for(state <- results){
                                    val info = state.getDFSInfo()
                                    for(elem <- info){
                                        val e = elem.getElement()
                                        val s = elem.getStatus()

                                        verticies(findVertex(e)).updateStatus(State.statusToObject(s))
                                        verticies(findVertex(e)).updateActive(e == state.getActiveElement())
                                    }

                                    Thread.sleep(500)
                                    showingDFS = true
                                    Swing.onEDT(graphGraphic.repaint())
                                }

                                var allReached = true
                                for(v <- verticies){
                                    if(v.status().getStatus() != "black"){
                                        allReached = false
                                    }
                                }

                                dfsDialogue(allReached)
                                showingDFS = false
                            }
                        }

                        runDFS.start()  

                        showingDFS = false
                        graphGraphic.repaint()
                    }
                }
            }
            
            val compsize = new Dimension(1100, 100)
            preferredSize = compsize
            minimumSize = compsize
            maximumSize = compsize
        }
         
        var generatorsLabel= new BoxPanel(Orientation.Vertical){
            contents += new Label("Generators:"){
                font = new Font("Dialog", java.awt.Font.BOLD, 18)
            }
            
            var i = 0
            for(g <- generators){
                contents += new Label("    " + g.toString){
                    foreground = colours(i)
                    font = new Font("Dialog", java.awt.Font.BOLD, 18)
                }
                i += 1
            }

            val compsize = new Dimension(200, 900)
            preferredSize = compsize
            minimumSize = compsize
            maximumSize = compsize
        }
        
        contents = new BorderPanel{
            add(graphGraphic, BorderPanel.Position.Center)
            add(controls, BorderPanel.Position.South)
            add(generatorsLabel, BorderPanel.Position.East)
        }

        size = new Dimension(1200, 1000)
    }
}

case class UpdateDFSGraph() extends Event