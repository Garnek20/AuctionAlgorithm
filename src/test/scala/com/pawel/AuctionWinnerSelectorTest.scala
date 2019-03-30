package com.pawel

import com.pawel.AuctionWinnerSelector.{Buyer, select}
import org.scalatest.{FunSuite, GivenWhenThen, Matchers}

class AuctionWinnerSelectorTest
    extends FunSuite
    with Matchers
    with GivenWhenThen {

  test("Provided example solution") {
    Given("Preserve price, buyers and their bid prices")
    val preservePrice = 100
    val buyers = List(
      Buyer("A", Set(110, 130)),
      Buyer("B", Set()),
      Buyer("C", Set(125)),
      Buyer("D", Set(105, 115, 90)),
      Buyer("E", Set(132, 135, 140))
    )

    When("Selection is executed")
    val Some((winnerName, winningPrice)) =
      select(preservePrice, buyers)

    Then("The winner gets the highest price of the closest non-winning buyer")
    winnerName should be("E")
    winningPrice should be(130)
  }

  test("Empty list of buyers") {
    val result =
      select(100, List())

    result should be(None)
  }

  test("Winning price equal for multiple buyers") {
    Given("Preserve price and non-winning buyers highest price are equal for different buyers")
    val preservePrice = 100

    val buyers = List(
      Buyer("A", Set(110, 130)),
      Buyer("B", Set()),
      Buyer("C", Set(130)),
      Buyer("D", Set(105, 115, 90)),
      Buyer("E", Set(132, 135, 140))
    )
    When("Selection is executed")
    val Some((winnerName, winningPrice)) =
      select(preservePrice, buyers)

    Then("The winner gets the highest price of those 2 non-winning buyers")
    winnerName should be("E")
    winningPrice should be(130)
  }

  test("Only one buyer offers price above reserved price") {
    Given("Preserve price, buyers and their bid prices")
    val preservePrice = 100

    val buyers = List(
      Buyer("A", Set(110)),
      Buyer("B", Set()),
      Buyer("C", Set(99)),
      Buyer("D", Set(80, 93)),
      Buyer("E", Set(0))
    )
    When("Selection is executed")
    val Some((winnerName, winningPrice)) =
      select(preservePrice, buyers)

    Then("The winner gets the reserve price")
    winnerName should be("A")
    winningPrice should be(100)
  }

  test("No buyer offered sufficient bid price") {
    Given("Preserve price, buyers and their bid prices")
    val preservePrice = 100

    val buyers = List(
      Buyer("A", Set(88)),
      Buyer("B", Set()),
      Buyer("C", Set(99)),
      Buyer("D", Set(80, 93)),
      Buyer("E", Set(0))
    )
    When("Selection is executed")
    val result = select(preservePrice, buyers)

    Then("There is no winner for this auction")
    result should be(None)
  }
}
