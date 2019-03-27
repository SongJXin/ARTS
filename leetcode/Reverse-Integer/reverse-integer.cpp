//Runtime: 12 ms, faster than 97.82% of C++ online submissions for Reverse Integer.
//Memory Usage: 9 MB, less than 95.95% of C++ online submissions for Reverse Integer.
class Solution {
public:
    int reverse(int x) {
	bool minus = false;
	queue<int> inter;
	if (x < 0) {
		minus = true;
	}
	while (x != 0)
	{
		if (minus) {
			inter.push(- (x % 10));
		}
		else {
			inter.push(x % 10);
		}
		x = x / 10;
	}
	int result = 0;
	while (!inter.empty()) {
		result = result * 10 + inter.front();
		inter.pop();
		if (inter.size() == 1) {
			if (result > 214748364 || result < -214748364) {
				return 0;
			}
			else if (result == 214748364 || result == -214748364) {
				if (minus && inter.front() >= 8) {
					return 0;
				}
				if (inter.front() >= 7) {
					return 0;
				}
			}
		}
	}
	if (minus) {
		result = -result;
	}
	return result;
    };
};