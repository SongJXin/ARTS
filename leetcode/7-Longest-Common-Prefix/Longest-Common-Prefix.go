//Runtime: 0 ms, faster than 100.00% of Go online submissions for Longest Common Prefix.
//Memory Usage: 2.4 MB, less than 64.00% of Go online submissions for Longest Common Prefix.
func longestCommonPrefix(strs []string) string {
    result := ""
	if len(strs) <=0{
		return ""
	}
	if len(strs) == 1{
		return strs[0]
	}
	min1 := len(strs[0])
	for i := 0;i < len(strs); i++{
		if len(strs[i]) < min1{
			min1 = len(strs[i])
		}
	}
	for i := 0 ;i < min1;i++{
		judge := true
		preChar := strs[0][i]
		for j := 0;j<len(strs);j++{
			if strs[j][i] != preChar{
				judge = false
				break
			}
		}
		if judge{
			result = strs[0][0:i+1]
		}else{
			break
		}
	}
	return result
}