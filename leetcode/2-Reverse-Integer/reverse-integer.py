# Runtime: 40 ms, faster than 100.00% of Python3 online submissions for Reverse Integer.
# Memory Usage: 13.2 MB, less than 5.71% of Python3 online submissions for Reverse Integer.

class Solution:
    def reverse(self, x: int) -> int:
        result = 0
        if x < 0:
            strx = str(-x)
            result = -int(strx[::-1])
        else:
            strx = str(x)
            result = int(strx[::-1])
        if result >= 2147483648 :
            return 0
        if result < -2147483648:
            return 0
        return result