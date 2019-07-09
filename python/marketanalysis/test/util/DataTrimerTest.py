import unittest
import sys, os
sys.path.append(os.path.dirname(os.path.abspath(__file__)) + "/../../src/util")
from DataTrimer import DataTrimer

class DataTrimerTest(unittest.TestCase):

    def test_trim_day(self):
        data = [1, 2, 3, 4, 5, 6, 7, 8, 9]
        result = DataTrimer.trim_day(data, 2, 4)
        expected = [(1 + 2 + 3 + 4) / 4, (3 + 4 + 5 + 6) / 4, (5 + 6 + 7 + 8) / 4]
        assert result == expected, "trim_day test"
    
    def test_trim_data_seq(self):
        data1 = [1, 2, 3, 4, 5, 6, 7, 8, 9]
        data2 = [2, 3, 4, 5, 6, 7, 8, 9, 10]
        data3 = [3, 4, 5, 6, 7, 8, 9, 10, 11]
        data = [data1, data2, data3]
        result = DataTrimer.trim_data_seq(data, 2, 4)
        expected1 = [(1 + 2 + 3 + 4) / 4, (3 + 4 + 5 + 6) / 4, (5 + 6 + 7 + 8) / 4]
        expected2 = [(2 + 3 + 4 + 5) / 4, (4 + 5 + 6 + 7) / 4, (6 + 7 + 8 + 9) / 4]
        expected3 = [(3 + 4 + 5 + 6) / 4, (5 + 6 + 7 + 8) / 4, (7 + 8 + 9 + 10) / 4]
        expected = [expected1, expected2, expected3]
        assert result == expected, "trim_data_seq test"

if __name__ == "__main__":
    unittest.main()
