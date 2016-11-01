package com.fk.neuralnet


object Protocol {
  case class Result(nodes: List[GraphNode], edges: List[GrapthEdge], neuralNetLayerList: List[Layer])
  case class GraphNode(id: Int, label: String, image: String, actFunc: String, name: String)
  case class GrapthEdge(from: String, to: String, arrows: String, label: String)
  case class Layer(layerNum: Int, neuralNetNodeList: List[GraphNode])

  case class PredictionResult(prediction: Int, activations: List[Double], numOutputNodes: Int, inputsNormalized: Boolean)
}
