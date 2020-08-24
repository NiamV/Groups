import scala.swing._
import scala.swing.event._
import java.awt.Color

object Drawing extends SimpleSwingApplication{
    def top = new MainFrame{
        val graphic = new Component{
            var posx = 0
            var posy = 0

            override def paintComponent(g: Graphics2D){
                super.paintComponent(g)

                val text = "You're Here"

                val fontMetrics = g.getFontMetrics()

                val strCoordx = posx - (fontMetrics.stringWidth(text) / 2)
                val strCoordy = posy + (fontMetrics.getHeight() / 2)

                g.drawString(text, strCoordx, strCoordy)
                g.drawOval(posx, posy, 20, 20)

                g.drawOval(100, 100, 20, 20)
            }

            listenTo(mouse.clicks)
            listenTo(mouse.moves)
            
            reactions += {
                case e: MouseClicked => {
                    posx = e.point.x
                    posy = e.point.y
                    repaint()
                }
                case e: MouseDragged => {
                    posx = e.point.x
                    posy = e.point.y
                    repaint()
                }
            }

            border = new javax.swing.border.LineBorder(new Color(0,0,0))
        }

        contents = new BorderPanel{
            // contents += new Label("Drawing")
            // contents += graphic

            add(new Label("hi"), BorderPanel.Position.North)
            add(graphic, BorderPanel.Position.Center)
        }

        size = new Dimension(500, 500)
    }
}




// generatorsLabel.contents.clear()
                    // generatorsLabel.contents += new Label("Generators:")
        
                    // var i = 0
                    // for(g <- generators){
                    //     generatorsLabel.contents += new Label("    " + g.toString){
                    //         foreground = colours(i)
                    //     }
                    //     i += 1
                    // }

                    // generatorsLabel.revalidate()
                    // generatorsLabel.repaint()