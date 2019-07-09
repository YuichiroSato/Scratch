import unittest
import sys, os
sys.path.append(os.path.dirname(os.path.abspath(__file__)) + "/../../src/util")
from Correlogram import Correlogram

class CorrelogramTest(unittest.TestCase):

    def test_calculate(self):
        data = [0,1,2,3,4,5,6,7,8,9]
        c = Correlogram(data)
        result = c.calculate(4, 1, 2, 4)
        assert 5 == len(result), "test_autocorrelation"
        assert 1.0 == result[0], "test_autocorrelation"
        assert 1.0 == result[1], "test_autocorrelation"
        assert 1.0 == result[2], "test_autocorrelation"
        assert 1.0 == result[3], "test_autocorrelation"
        assert 0.0 == result[4], "test_autocorrelation"

    def test_is_significant(self):
        result1 = Correlogram.is_significant(0.1, 10)
        result2 = Correlogram.is_significant(1, 10)
        result3 = Correlogram.is_significant(-0.1, 10)
        result4 = Correlogram.is_significant(-1, 10)
        assert not result1, "test_is_signifficant1"
        assert result2, "test_is_signifficant2"
        assert not result3, "test_is_signifficant3"
        assert result4, "test_is_signifficant4"

if __name__ == "__main__":
    unittest.main()
