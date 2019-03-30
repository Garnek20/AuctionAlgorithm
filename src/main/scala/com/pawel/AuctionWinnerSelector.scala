package com.pawel

import scala.util.Try

object AuctionWinnerSelector {

  def select(reservePrice: Double,
             buyers: List[Buyer]): Option[(String, Double)] = {
    // finding max price for each buyer
    val buyersMaxPrices = buyers.map(
      buyer =>
        (buyer.name,
         Try(buyer.bidsPrice.max).toOption
           .getOrElse(Double.MinPositiveValue)))

    // finding buyer with the highest bid price
    val (winningBuyerName, winningBuyerMaxPrice) =
      Try {
        buyersMaxPrices.maxBy { case (_, price) => price }
      }.toOption.getOrElse(("", Double.MinPositiveValue))

    if (winningBuyerMaxPrice > reservePrice) {
      // finding the closest non-winning bid price
      val (_, winingPrice) = buyersMaxPrices.minBy {
        case (name, maxPrice) =>
          if (name != winningBuyerName) winningBuyerMaxPrice - maxPrice
          else Double.MaxValue
      }
      Some(
        (winningBuyerName,
         if (winingPrice > reservePrice) winingPrice else reservePrice))
    } else {
      None
    }
  }

  final case class Buyer(name: String, bidsPrice: Set[Double])

}
