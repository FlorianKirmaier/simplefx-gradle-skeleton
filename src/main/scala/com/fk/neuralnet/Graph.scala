package com.fk.neuralnet

import simplefx.all._
import simplefx.core._
import simplefx.experimental._

class Graph extends Pane {
  styleClass ::= "graph"

  class Corner(posx: Double, posy: Double) extends Group { CORNER =>
    def pos = layoutXY
    @Bind var text = ""
    @Bind var innerText = ""
    @Bind var radius = 20.0
    @Bind var image: Image = null

    styleClass ::= "corner"
    onClick --> requestFocus
    layoutXY = (posx,posy)
    Δ(layoutXY) <-- Δ(dragDistance)

    this <++ new Circle {
      styleClass  ::= "circle"
      this.radius <-- CORNER.radius
      fill        <-- (if(CORNER.image == null) Color.TRANSPARENT else new ImagePattern(CORNER.image))
    }
    @Bind val label = new Label {
      this.text <-- CORNER.text
      fontSize   =  15
      layoutXY  <-- (0,radius + 5) + labXY - (labW / 2, 0)
    }
    this <++ new Label {
      this.text <-- CORNER.innerText
      fontSize   =  15
      layoutXY  <-- labXY - labWH / 2
    }
    when(radius > 10) ==> { this <++ label}
  }

  class Edge(startC: Corner, endC: Corner) extends Group { EDGE =>
    @Bind var text = ""
    styleClass ::= "edge"
    onClick --> requestFocus

    @Bind val displayedFocused = <--(focused || startC.focused || endC.focused)
    when(displayedFocused) ==> {
      styleClass <++ "edge-focused"
    }
    def lineWidth = (if(displayedFocused) 2 else 1) * mini(startC.radius, endC.radius) / 20

    def dist = endC.pos - startC.pos
    def startPos = startC.pos + dist.normalize * startC.radius
    def endPos   = endC  .pos - dist.normalize * (endC  .radius + 5)

    def maxArrowLength = mini(startC.radius / 3, endC.radius / 3,20)
    def arrowLength = minmax(0.0,dist.length * 0.1,maxArrowLength)

    this <++ new Group {
      styleClass ::= "arrow"

      this <++ new Line {
        strokeWidth <-- lineWidth
        start       <-- startPos
        end         <-- endPos
      }
      this <++ new Line {
        strokeWidth <-- lineWidth
        start       <-- endPos
        end         <-- endPos - dist.normalize*arrowLength + dist.normalize.orthogonal*arrowLength
      }
      this <++ new Line {
        strokeWidth <-- lineWidth
        start       <-- endPos
        end         <-- endPos - dist.normalize*arrowLength - dist.normalize.orthogonal*arrowLength
      }
    }
    @Bind val label = new Label {
      this.text <-- EDGE.text
      transform <-- Translate((startC.pos + endC.pos) / 2) * Rotate(radToDegrees(dist.angle)) * Translate(-labW / 2, -5 - labH)
    }
    when(arrowLength > 5) ==> { this <++ label}
  }


}
