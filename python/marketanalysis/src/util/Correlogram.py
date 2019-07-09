import numpy
import math

from Correlation import Correlation
from DataTrimer import DataTrimer

class Correlogram:

    # data: Data to calculate correlogram.
    def __init__(self, data):
        self.data = data

    # window_size: The number of data to be meaned.
    # trim_interval: The index interval to calculate mean.
    # n: The number of trimed data used to calculate correlation.
    # order: The maximum order of correlation to calculate.
    def calculate(self, window_size, trim_interval, n, order):
        trimed = DataTrimer.trim_day(self.data, trim_interval, window_size)
        c = Correlation(n)
        result = [0 for i in range(0, order + 1)]
        for k in range(0, order + 1):
            t = 0
            ac = 0
            while c.has_more(trimed, t, k):
                ac = ac + c.autocorrelation(trimed, t, k)
                t = t + 1
            if t > 0:
                result[k] = ac / t
        return result

    @staticmethod
    def is_significant(correlation, data_size):
        return math.fabs(correlation) > 1.96 / math.sqrt(data_size)
