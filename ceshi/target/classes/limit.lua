local key = KEYS[1] --限流KEY（一秒一个）
local limit = tonumber(ARGV[1])        --限流大小
local current = tonumber(redis.call('get', key) or "0")
if current + 1 > limit then --如果超出限流大小
    redis.call("INCRBY", key,"1") -- 如果不需要统计真是访问量可以不加这行
    return 0
else  --请求数+1，并设置2秒过期
    redis.call("INCRBY", key,"1")
    redis.call("expire", key,2) --时间窗口最大时间后销毁键
    return 1
end