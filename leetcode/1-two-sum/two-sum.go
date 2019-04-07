//Runtime: 36 ms, faster than 41.70% of Go online submissions for Two Sum.
//Memory Usage: 2.9 MB, less than 100.00% of Go online submissions for Two Sum.

func twoSum(nums []int, target int) []int {
	for i := 0; i < len(nums); i += 1{
		for j := i + 1; j < len(nums); j += 1{
			if nums[i] + nums[j] == target{
				return []int {i,j}
			}
		}
	}
	return []int {0,1}
}