import numpy as np
from scipy.stats import norm

# sampling
rng = np.random.default_rng()
print(rng.normal(0, 0.1, 5))

# probability density function
xs = np.linspace(-2, 2, 5)
print(norm.pdf(xs))

