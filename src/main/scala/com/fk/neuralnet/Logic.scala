package com.fk.neuralnet

import simplefx.core._
import simplefx.all._
import com.neovisionaries.ws.client._
import Protocol._


trait Logic { this: View =>

  val server = "localhost:8080"


  val samplenames = List(
    "XOR"                   -> "XorExample",
    "Iris flower"           -> "CSVExample",
    "Speed Dating"          -> "SpeedDating",
    "Wine Classifier"       -> "WineClassifier",
    "MLP Classifier Saturn" -> "MLPClassifierMoon",
    "Regression Sum "       -> "RegressionSum",
    "Tic Tac Toe"           -> "TicTacToe")


  var wsDisp = simplefx.core.Disposer.empty
  openSample(samplenames.head._2) // open the first sample on startup

  def predict(): Unit = {
    println("calling predict!")
    val values: List[String] = predictionFields.map(_.text)
    val valuesText = values.reduce(_ + "," + _)

    val url = s"http://$server/prediction?values=$valuesText"
    val resStr = scala.io.Source.fromURL(url).mkString

    val res = upickle.default.read[PredictionResult](resStr)
    res.activations.zipWithIndex.map { case (v,i) =>
      val corner = currentGraph.cornerMap(i)
      corner.image = null
      corner.innerText = v.toString
    }

    currentGraph.predicted = res.prediction
  }


  def openSample(samplename: String) {
    wsDisp.dispose
    val graph = new NeuralNetworkGraph(this)
    currentGraph = graph

    val ws = new WebSocketFactory().createSocket(s"ws://$server/counter")
    ws.addListener(new WebSocketAdapter {
      override def onTextMessage(client: WebSocket, str: String): Unit = {
        import upickle.default._
        try {
          val res = read[Result](str)
          simplefx.core.inFX{
            println("updating!")
            graph.updateContent(res)
          }
        } catch {
          case e: Throwable =>
            println(s"error while parsing: " + e.getMessage)
            println("### start ###")
            println(str)
            println("### end ###")
        }
      }
    })
    ws.connect()
    ws.sendText("{\"name\":\"" + samplename + "\"}")
    wsDisp = simplefx.core.Disposer(ws.disconnect())
  }
}
