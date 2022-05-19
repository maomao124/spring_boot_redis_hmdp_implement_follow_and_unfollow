package mao.spring_boot_redis_hmdp.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mao.spring_boot_redis_hmdp.dto.Result;
import mao.spring_boot_redis_hmdp.entity.Follow;
import mao.spring_boot_redis_hmdp.mapper.FollowMapper;
import mao.spring_boot_redis_hmdp.service.IFollowService;
import mao.spring_boot_redis_hmdp.utils.UserHolder;
import org.springframework.stereotype.Service;


@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService
{

    @Override
    public Result follow(Long followUserId, Boolean isFollow)
    {
        //空值判断
        if (followUserId == null)
        {
            return Result.fail("关注的用户id不能为空");
        }
        if (isFollow == null)
        {
            return Result.fail("参数异常");
        }
        //获取当前用户的id
        Long userID = UserHolder.getUser().getId();
        //判断是关注还是取消关注
        if (isFollow)
        {
            //是关注
            //加关注
            Follow follow = new Follow();
            //设置关注的用户id
            follow.setFollowUserId(followUserId);
            //设置当前用户的id
            follow.setUserId(userID);
            //保存到数据库
            boolean save = this.save(follow);
            //判断是否关注失败
            if (!save)
            {
                return Result.fail("关注失败");
            }
        }
        else
        {
            //不是关注，取消关注
            //删除数据库里的相关信息
            QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("follow_user_id", followUserId).eq("user_id", userID);
            //删除
            boolean remove = this.remove(queryWrapper);
            if (!remove)
            {
                return Result.fail("取消关注失败");
            }
        }
        //返回
        return Result.ok();

    }

    @Override
    public Result isFollow(Long followUserId)
    {
        //获取当前用户的id
        Long userID = UserHolder.getUser().getId();
        //查数据库
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("follow_user_id", followUserId).eq("user_id", userID);
        long count = this.count(queryWrapper);
        //返回
        return Result.ok(count > 0);
    }
}
