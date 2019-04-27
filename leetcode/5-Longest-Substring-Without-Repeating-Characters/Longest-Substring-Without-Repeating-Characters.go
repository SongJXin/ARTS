//Runtime: 4 ms, faster than 100.00% of Go online submissions for Longest Substring Without Repeating Characters.
//Memory Usage: 2.6 MB, less than 90.46% of Go online submissions for Longest Substring Without Repeating Characters.
func lengthOfLongestSubstring(s string) int {
    	max := 0
	subS := ""
	leftIndex := 0
	for i:=0;i< len(s);i++{
		repeat := strings.IndexAny(subS, s[i:i+1])
		if repeat != -1{
			leftIndex = leftIndex + repeat + 1
		}
		subS = s[leftIndex: i + 1]
		if len(subS) > max {
			max = len(subS)
		}
	}
	return max
}