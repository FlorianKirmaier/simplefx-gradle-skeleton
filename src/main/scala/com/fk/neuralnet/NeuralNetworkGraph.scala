package com.fk.neuralnet

import simplefx.all._
import simplefx.core._
import simplefx.experimental._
import Protocol._

class NeuralNetworkGraph(view: View) extends Graph{ GRAPH =>
  import view._

  prefWH = (Int.MaxValue,Int.MaxValue)
  hgrow = Priority.SOMETIMES

  @Bind var predicted = -1
  @Bind var predictedCorner: Corner = <--(if(predicted == -1) null else cornerMap(predicted))
  predictedCorner ==> { corner =>
    if(corner != null) {
      corner.styleClass <++ "corner-predicted"
    }
  }

  var cornerMap = Map[Int,Corner]()
  def updateContent(x: Result) = {

    val layers = x.neuralNetLayerList
    val layersCount = layers.length
    val maxLayerSize = layers.map(_.neuralNetNodeList.length).max

    val graphRadius = minmax(0,200 / maxLayerSize,40)
    val xOffset = (1200 - layersCount * graphRadius * 6) / 2

    layers.zipWithIndex.map { case (layer,x) =>
      val nodes = layer.neuralNetNodeList
      val height = nodes.length
      nodes.zipWithIndex.foreach { case (node,i) =>
        val yOffset = i + (maxLayerSize - height) / 2
        val id = node.id
        val corner = cornerMap.getOrElse(id, new Corner(xOffset + x * graphRadius * 6, 100 + yOffset * graphRadius * 3 + (if(x % 2 == 0) graphRadius else 0)))
        corner.text = node.label
        corner.image = Image.cached(node.image)
        corner.radius = graphRadius

        cornerMap += id -> corner
      }
    }

    predictionNames = layers.head.neuralNetNodeList.map{x => x.name}

    updateCorners(x.edges)

    children = (cornerMap.values ++ edgeMap.values).toList
  }

  var edgeMap = Map[(Corner, Corner), Edge]()
  def updateCorners(x: List[GrapthEdge]): Unit = {
    x.map { info =>
      val start = cornerMap(info.from.toInt)
      val end   = cornerMap(info.to  .toInt)

      val key = (start,end)
      val edge = edgeMap.getOrElse(key, new Edge(start,end) {
      })
      edgeMap += key -> edge

      edge.text = info.label
    }
  }
}