package akkasmpp
import java.net.InetSocketAddress
import java.nio.charset.StandardCharsets

import akka.actor.{Actor, ActorSystem, Props}
import akka.io.{IO, Tcp}
import akka.pattern.ask
import akka.util.Timeout
import akkasmpp.actors.SmppClient._
import akkasmpp.actors.{SmppClient, SmppClientConfig, SmppPartials, SmppServer, SmppServerHandler}
import akkasmpp.protocol.DataCodingScheme.DataCodingScheme
import akkasmpp.protocol.EsmClass.{MessageType, MessagingMode}
import akkasmpp.protocol.NumericPlanIndicator._
import akkasmpp.protocol._

import scala.concurrent.duration._
import scala.util.Success

object Demo extends App {

//  implicit val actorSystem = ActorSystem("demo")
  implicit val actorSystem = ActorSystem("demo")
  val manager = IO(Tcp)

  implicit val t: Timeout = 5.seconds
  import scala.concurrent.ExecutionContext.Implicits.global

  /*
    Demo server
   */

//  SmppServer.run("localhost", 2775) { (wire, connection) =>
//    new SmppServerHandler(wire, connection) with SmppPartials {
//
//      override def bound = enquireLinkResponder orElse processSubmitSm
//
//      def processSubmitSm: Receive = {
//        case wire.Event(ss: SubmitSm) =>
//          log.info(s"SubmitSm with TLVs of ${ss.tlvs}")
//          sender ! wire.Command(SubmitSmResp(
//            CommandStatus.ESME_ROK, ss.sequenceNumber, Some(new COctetString("abcde"))))
//      }
//    }
//  }



//    Demo client

//  for (creds <- List(("smppclient1", "password"), ("user2", "pass2"))) {

//  implicit def str2CoctetString(s: String): COctetString = new COctetString(s)(java.nio.charset.Charset.forName("ISO_8859_9"))

  for (creds <- List(("dkrb", "y547b2"))) {
    val client = actorSystem.actorOf(SmppClient.props(SmppClientConfig(new InetSocketAddress("10.230.16.10", 2885)), {
      case d: DeliverSm =>
        println(s"Incoming message $d")
        DeliverSmResp(CommandStatus.ESME_ROK, d.sequenceNumber, None)
    }))
    client ! Bind(creds._1, creds._2, None, Transceiver, TypeOfNumber.NationalSpecific)
//    val f = client ? SendMessage("this is message", Did("9898"), Did("+996558047694"), Option[DataCodingScheme](DataCodingScheme.Latin1))
//    f.mapTo[SendMessageAck].onComplete { ack =>
//      println("f on complete.")
//      println(ack)
//    }
//    (client ? SendMessage("hahahaa", Did("9898", TypeOfNumber.NationalSpecific, NumericPlanIndicator.Unknown), Did("+996558047694", TypeOfNumber.International, NumericPlanIndicator.E164))) onComplete { x =>
//      println("client sending message completed.")
//      println(x)
//    }
    val test = "Привет."
//    val msg = new String(test.getBytes("Cp1251"), "UTF-16")
//    println(msg)
    (client ? SendMessage(test, Did("996558047694", TypeOfNumber.International, NumericPlanIndicator.E164),
        Did("9898", TypeOfNumber.Unknown, NumericPlanIndicator.E164))) onComplete { x =>
      println("client sending message completed.")
      println(x)
    }


//      implicit def str2CoctetString(s: String): COctetString = new COctetString(s)(java.nio.charset.Charset.forName("ASCII"))

//
//    (client ? SendRawPdu(EnquireLink)) onComplete { x =>
//      println("client sending raw pdu completed.")
//      println(x)
//    }
  }


//  val c = SmppClient.connect(SmppClientConfig(new InetSocketAddress("10.230.16.10", 2885)), {
//    case d: SmscRequest => GenericNack(CommandStatus.ESME_RINVCMDID, d.sequenceNumber)
//  }, "client")
//
//  c ? Bind("dkrb", "y547b2") onComplete { x =>
//    val f = c ? SendRawPdu(SubmitSm(_, ServiceType.Default, TypeOfNumber.Unknown, NumericPlanIndicator.E164, new COctetString("9898".getBytes("ASCII")), TypeOfNumber.International,
//      NumericPlanIndicator.E164, new COctetString("996558047694".getBytes("ASCII")), EsmClass(MessagingMode.Default, MessageType.NormalMessage), 0x0,
//      Priority.Level0, NullTime, NullTime, RegisteredDelivery(0x0.toByte), false, DataCodingScheme.Cyrllic, 0x0, 0x0, new OctetString("Тест".getBytes("Cp1251")), Nil))
//
//    f.onComplete {
//      case Success(SubmitSmResp(commandStatus, _, _)) => println(s"command status was $commandStatus")
//    }
//  }


  //  val c = SmppClient.connect(SmppClientConfig(new InetSocketAddress("ec2-184-73-153-156.compute-1.amazonaws.com", 2775)), {
//  val c = SmppClient.connect(SmppClientConfig(new InetSocketAddress("10.230.16.10", 2885)), {
//    case d: SmscRequest => GenericNack(CommandStatus.ESME_RINVCMDID, d.sequenceNumber)
//  }, "client")
//

//
//  c ? Bind("any", "any") onComplete { x =>
//    val f = c ? SendRawPdu(SubmitSm(_, "tyntec", TypeOfNumber.International, NumericPlanIndicator.E164, "15094302095", TypeOfNumber.International,
//    NumericPlanIndicator.E164, "15094302095", EsmClass(MessagingMode.Default, MessageType.NormalMessage), 0x0,
//    Priority.Level0, NullTime, NullTime, RegisteredDelivery(0x0.toByte), false, DataCodingScheme.SmscDefaultAlphabet, 0x0, 0x0, OctetString.empty, Nil))
//
//    f.onComplete {
//      case Success(SubmitSmResp(commandStatus, _, _)) => println(s"command status was $commandStatus")
//    }
//  }

//  actorSystem.actorOf(Props(new Actor() {
//    override def receive: Actor.Receive = ???
//  }))



}
