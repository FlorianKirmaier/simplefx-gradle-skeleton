package com.fk.neuralnet

import simplefx.all._
import simplefx.core._
import simplefx.experimental._
import Protocol._

class View extends StackPane with Logic {

  @Bind val header = new Label("Exposed! Neural Network in jpro") {
    styleClass ::= "header"
    prefWidthProp := Int.MaxValue
  }

  @Bind val  selection = new VBox {
    styleClass ::= "selection"
    spacing = 5
    class Box(x: String) extends Button(x) { prefWidthProp = 200}
    samplenames.map { case (text,name) =>
      <++(new Box(text){ onAction --> openSample(name)})
    }
  }

  @Bind var predictionNames = List[String]()
  predictionNames --> updatePrediction
  @Bind var predictionFields = List.empty[TextField]
  @Bind val  prediction = new ScrollPane(new VBox {
    styleClass ::= "predict"
    spacing = 5
    @Bind val predictButton = new Button("Predict") {
      styleClass ::= "predict-button"
      prefWidthProp = 200
      onAction --> predict()
    }

    children <-- predictButton :: predictionFields
  }) {
    hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER
  }

  def updatePrediction(x: List[String]): Unit = {
    predictionFields = x map { text =>
      new TextField { promptText = text; javafx.scene.layout.VBox.setMargin(this,Insets(5))}
    }
  }

  val leftside = new VBox(selection,prediction) {
    minWidthProp = 200
    spacing = 50
    hgrow = Priority.ALWAYS
  }

  val main = new HBox(leftside,testgraph) {
    spacing = 10
  }

  @Bind var currentGraph = new NeuralNetworkGraph(this)
  @Bind val testgraph = new StackPane {
    currentGraph ==> {
      this <++ currentGraph
    }
  }

  <++(new VBox(header,main) {
    styleClass ::= "main"
    spacing = 10
  })

  def updateContent(x: Result) = {

  }
}
