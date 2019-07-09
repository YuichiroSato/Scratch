import unittest
import sys, os
sys.path.append(os.path.dirname(os.path.abspath(__file__)) + "/../../src/util")
import datetime
from Parsers import SettingParser

class ParserTest(unittest.TestCase):

    def setUp(self):
        parser = SettingParser("./")
        parser.load_setting("parser_test")
        self.Parser = parser

    def test_model(self):
        assert self.Parser.model() == "test_model", "test_model"

    def test_date(self):
        assert self.Parser.date() == datetime.date(2000, 4, 10), "test_date"

    def test_is_am(self):
        assert self.Parser.is_am() == True, "test_is_am"

    def test_code(self):
        assert self.Parser.code() == 1000, "test_code"

    def test_data_size(self):
        assert self.Parser.data_size() == 10, "test_data_size"
    
    def test_trim_interval(self):
        assert self.Parser.trim_interval() == 20, "test_trim_interval"
    
    def test_window_size(self):
        assert self.Parser.window_size() == 20, "test_window_size"
    
    def test_iter(self):
        assert self.Parser.iter() == 5, "test_iter"
    
    def test_chains(self):
        assert self.Parser.chains() == 5, "test_chains"
    
    def test_output_file_name(self):
        assert self.Parser.output_file_name() == "hoge", "test_output_file_name"

    def test_get(self):
        assert self.Parser.get("key") == "val", "test_get"

if __name__ == "__main__":
    unittest.main()
