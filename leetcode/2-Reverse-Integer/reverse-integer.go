// Runtime: 4 ms, faster than 100.00% of Go online submissions for Reverse Integer.
// Memory Usage: 2.3 MB, less than 86.36% of Go online submissions for Reverse Integer.
func reverse(x int) int {
	minus := false
	if x < 0{
		minus = true
	}
	var inter []int
	for x != 0{
		if minus{
			inter = append(inter, -(x%10))
		}else{
			inter = append(inter,x%10)
		}
		x=x/10
	}
	var result int
	result = 0
	for i:=0;i < len(inter);i=i+1 {
		result = result * 10 + inter[i]
		if i == len(inter) - 2{
			if result > 214748364 {
				return 0
			}
			if result == 214748364{
				if minus&&inter[i] >= 8{
					return 0
				}
				if inter[i]>=7{
					return 0
				}
			}
		}
	}
	if minus{
		result = -result
	}
	return result
}