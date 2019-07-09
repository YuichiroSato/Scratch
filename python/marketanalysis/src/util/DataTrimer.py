import numpy

class DataTrimer:

    @staticmethod
    def trim_day(data, trim_interval, window_size):
        data_length = int(len(data) / trim_interval)
        meaned = []
        while len(data) > window_size:
            head = data[:window_size]
            meaned = meaned + [numpy.mean(head)]
            data = data[trim_interval:]
        return meaned
    
    @staticmethod
    def trim_data_seq(data, trim_interval, window_size):
        d = [[0] for i in range(len(data))]
        for i in range(len(d)):
            d[i] = DataTrimer.trim_day(data[i], trim_interval, window_size)
        return d
