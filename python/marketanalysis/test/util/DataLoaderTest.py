import unittest
import sys, os
sys.path.append(os.path.dirname(os.path.abspath(__file__)) + "/../../src/util")
from DataLoader import DataLoader
import datetime

class DataLoaderTest(unittest.TestCase):

    def setUp(self):
        loader = DataLoader(".")
        self.loader = loader

    def test_to_filename(self):
        d = datetime.datetime(2000, 10, 10)
        d2 = datetime.datetime(2000, 1, 1)
        filename = self.loader.to_filename(d, True, 1000)
        filename2 = self.loader.to_filename(d2, True, 1000)
        assert filename == "20001010am_1000.csv", "to_filename test"
        assert filename2 == "20000101am_1000.csv", "to_filename test"

    def test_date_to_path(self):
        d = datetime.datetime(2000, 10, 10)
        d2 = datetime.datetime(2000, 1, 1)
        filename = self.loader.date_to_path(d)
        filename2 = self.loader.date_to_path(d2)
        assert filename == "/2000/10/20001010/", "date_to_path test"
        assert filename2 == "/2000/01/20000101/", "date_to_path test"

    def test_load_path(self):
        d = datetime.datetime(2000, 10, 10)
        d2 = datetime.datetime(2000, 1, 1)
        filename = self.loader.load_path(d, True, 1000)
        filename2 = self.loader.load_path(d2, False, 1000)
        assert filename == "./2000/10/20001010/20001010am_1000.csv", "load_path test"
        assert filename2 == "./2000/01/20000101/20000101pm_1000.csv", "load_path test"

    def test_read_data(self):
        d = datetime.datetime(2000, 1, 1)
        path = self.loader.load_path(d, True, 1000)
        data = self.loader.read_data(path)
        expected = [100, 200, 300, 400, 500]
        assert data == expected, "read_data test"

    def test_read_raw_data(self):
        d = datetime.datetime(2000, 1, 1)
        path = self.loader.load_path(d, True, 1000)
        data = self.loader.read_raw_data(path)
        expected = ['100', '200', '300', '400', '500']
        assert data == expected, "read_raw_data test"

    def test_get_data_files(self):
        d = datetime.datetime(2000, 1, 2)
        paths = self.loader.get_data_files(d, True, 1000, 2)
        expected = ["./2000/01/20000102/20000102am_1000.csv", "./2000/01/20000101/20000101am_1000.csv"]
        assert paths == expected, "data_files test"

    def test_load_data(self):
        d = datetime.datetime(2000, 1, 2)
        data = self.loader.load_data(d, True, 1000, 2)
        data2 = self.loader.load_data(d, False, 1000, 2)
        expected = [[102, 202, 302, 402, 502], [100, 200, 300, 400, 500]]
        expected2 = [[103, 203, 303, 403, 503], [101, 201, 301, 401, 501]]
        assert data == expected, "load_data test"

    def test_load_raw_data(self):
        d = datetime.datetime(2000, 1, 2)
        data = self.loader.load_raw_data(d, True, 1000, 2)
        data2 = self.loader.load_raw_data(d, False, 1000, 2)
        expected = [['102', '202', '302', '402', '502'], ['100', '200', '300', '400', '500']]
        expected2 = [['103', '203', '303', '403', '503'], ['101', '201', '301', '401', '501']]
        assert data == expected, "load_data test"

if __name__ == "__main__":
    unittest.main()
