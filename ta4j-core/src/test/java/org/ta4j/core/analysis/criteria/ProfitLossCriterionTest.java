/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Marc de Verdelhan, 2017-2019 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.ta4j.core.analysis.criteria;

import org.junit.Test;
import org.ta4j.core.*;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

import java.util.function.Function;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.ta4j.core.TestUtils.assertNumEquals;

public class ProfitLossCriterionTest extends AbstractCriterionTest {

    public ProfitLossCriterionTest(Function<Number, Num> numFunction) {
        super((params) -> new ProfitLossCriterion(), numFunction);
    }

    @Test
    public void calculateOnlyWithGainTrades() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 105, 110, 100, 95, 105);
        TradingRecord tradingRecord = new BaseTradingRecord(Order.buyAt(0, series, series.numOf(50)),
                Order.sellAt(2, series, series.numOf(50)), Order.buyAt(3, series, series.numOf(50)),
                Order.sellAt(5, series, series.numOf(50)));

        AnalysisCriterion profit = getCriterion();
        assertNumEquals(500 + 250, profit.calculate(series, tradingRecord));
    }

    @Test
    public void calculateOnlyWithLossTrades() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 95, 100, 80, 85, 70);
        TradingRecord tradingRecord = new BaseTradingRecord(Order.buyAt(0, series, series.numOf(50)),
                Order.sellAt(1, series, series.numOf(50)), Order.buyAt(2, series, series.numOf(50)),
                Order.sellAt(5, series, series.numOf(50)));

        AnalysisCriterion profit = getCriterion();
        assertNumEquals(-250 - 1500, profit.calculate(series, tradingRecord));
    }

    @Test
    public void calculateShortOnlyWithGainTrades() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 105, 110, 100, 95, 105);
        TradingRecord tradingRecord = new BaseTradingRecord(Order.sellAt(0, series, series.numOf(50)),
                Order.buyAt(2, series, series.numOf(50)), Order.sellAt(3, series, series.numOf(50)),
                Order.buyAt(5, series, series.numOf(50)));

        AnalysisCriterion profit = getCriterion();
        assertNumEquals(-(500 + 250), profit.calculate(series, tradingRecord));
    }

    @Test
    public void calculateShortOnlyWithLossTrades() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 95, 100, 80, 85, 70);
        TradingRecord tradingRecord = new BaseTradingRecord(Order.sellAt(0, series, series.numOf(50)),
                Order.buyAt(1, series, series.numOf(50)), Order.sellAt(2, series, series.numOf(50)),
                Order.buyAt(5, series, series.numOf(50)));

        AnalysisCriterion profit = getCriterion();
        assertNumEquals(250 + 1500, profit.calculate(series, tradingRecord));
    }

    @Test
    public void betterThan() {
        AnalysisCriterion criterion = getCriterion();
        assertTrue(criterion.betterThan(numOf(5000), numOf(4500)));
        assertFalse(criterion.betterThan(numOf(4500), numOf(5000)));
    }

    @Test
    public void testCalculateOneOpenTradeShouldReturnZero() {
        openedTradeUtils.testCalculateOneOpenTradeShouldReturnExpectedValue(numFunction, getCriterion(), 0);
    }

}
