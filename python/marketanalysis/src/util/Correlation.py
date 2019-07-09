import numpy
import math

class Correlation:

    # n: Number of data size to calculate correlation.
    def __init__(self, n):
        self.n = n

    # data: Data to calculate correlation.
    # t: Index to start calculation.
    # k: Order of correlation.
    def has_more(self, data, t, k):
        valid_params = t >= 0 and k >= 0 and self.n >= 0
        has_data1 = t < len(data) and t + self.n < len(data)
        has_data2 = t + k < len(data) and t + k + self.n < len(data)
        return valid_params and has_data1 and has_data2

    # data: Data to calculate correlation.
    # t: Index to start calculation.
    # k: Order of correlation.
    def autocorrelation(self, data, t, k):
        data1 = data[t:t + self.n]
        data2 = data[t + k:t + k + self.n]
        ac = self.autocovariance_at_t(data1, data2)
        v1 = numpy.var(data1)
        v2 = numpy.var(data2)
        if (v1 == 0 or v2 == 0):
            return 1
        return (ac / math.sqrt(v1)) * (1 / math.sqrt(v2))

    # size of data1 and data2 must be the same
    @staticmethod
    def autocovariance_at_t(data1, data2):
        m1 = numpy.mean(data1)
        m2 = numpy.mean(data2)
        arr = [0 for i in range(0, len(data1))]
        for i in range(0, len(arr)):
            arr[i] = (data1[i] - m1) * (data2[i] - m2)
        return numpy.mean(arr)
