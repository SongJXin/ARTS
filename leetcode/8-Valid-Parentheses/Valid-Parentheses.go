//Runtime: 0 ms, faster than 100.00% of Go online submissions for Valid Parentheses.
//Memory Usage: 6 MB, less than 11.96% of Go online submissions for Valid Parentheses.

func isValid(s string) bool {
	if len(s) == 0{
		return true
	}
	if len(s)%2 == 1{
		return false
	}
	if s[0]==')'||s[0]=='}'||s[0]==']'{
		return false
	}
	var st = ""
	for i:=0;i<len(s);i++{
		if s[i]=='('||s[i]=='{'||s[i]=='['{
			st = st + string(s[i])
			continue
		}
		if s[i] == ']' && st[len(st)-1] != '['{
			return false
		}
		if s[i] == ')' && st[len(st)-1] != '('{
			return false
		}
		if s[i] == '}' && st[len(st)-1] != '{'{
			return false
		}
		st = st[:len(st)-1]
	}
	if len(st) != 0{
		return false
	}
	return true
}