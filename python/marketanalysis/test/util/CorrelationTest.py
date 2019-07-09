import unittest
import sys, os
sys.path.append(os.path.dirname(os.path.abspath(__file__)) + "/../../src/util")
from Correlation import Correlation

class CorrelationTest(unittest.TestCase):

    def test_has_more(self):
        c = Correlation(5)
        data = [1,2,3,4,5,6,7,8,9,10]
        assert True == c.has_more(data, 0, 0), "test_has_more"
        assert True == c.has_more(data, 2, 2), "test_has_more"
        assert False == c.has_more(data, 3, 2), "test_has_more"
        assert False == c.has_more(data, 2, 3), "test_has_more"
        assert False == c.has_more(data, -1, 2), "test_has_more"
        assert False == c.has_more(data, 2, -1), "test_has_more"

    def test_autocorrelation(self):
        c = Correlation(4)
        data = [1,2,2,3,5,6,6,7]
        result = c.autocorrelation(data, 0, 4)
        assert 0.999999999 < result, "test_autocorrelation"
        assert 1.000000001 > result, "test_autocorrelation"

    def test_autocovariance_at_t(self):
        data1 = [1,2,2,3]
        data2 = [5,6,6,7]
        m1 = 2
        m2 = 6
        expected = ((1 - m1) * (5 - m2) + (3 - m1) * (7 - m2)) / 4 
        result = Correlation.autocovariance_at_t(data1, data2)
        assert expected == result, "test_autocovariance_at_t"

if __name__ == "__main__":
    unittest.main()
