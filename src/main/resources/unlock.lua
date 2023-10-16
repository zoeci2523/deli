-- 判断是否和指定的标示一致
if (redis.call('get', KEYS[1]) == ARGV[1]) then
    -- 如果一致则释放锁
    return redis.call('del', KEYS[1])
end
-- 如果不一致则什么都不做
return 0