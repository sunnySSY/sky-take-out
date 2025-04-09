package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "菜品管理")
@RestController
@RequestMapping("/admin/dish")
@Slf4j

public class DishController {
    @Autowired
    private DishService dishService;
    @PostMapping()
    @ApiOperation("新增菜品")
    public Result addDish(@RequestBody DishDTO dishDTO){
        log.info("新增菜品： {}", dishDTO);
        dishService.addDish(dishDTO);

        return Result.success();
    }

    @ApiOperation("分页查询菜品")
    @GetMapping("/page")
    public Result<PageResult> pageList(DishPageQueryDTO dishPageQueryDTO){
        log.info("分页查询结果：{}", dishPageQueryDTO);
        PageResult pageResult = dishService.page(dishPageQueryDTO); //返回的是一个整数和一个集合

        return Result.success(pageResult);
    }

    @ApiOperation("批量删除菜品")
    @DeleteMapping()
    public Result deleteById (@RequestParam List<Long> ids){
        log.info("被删除的菜品id为 {}" , ids);
        dishService.delete(ids);

        return Result.success();
    }

    @ApiOperation("根据id查询菜品")
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id){

        return Result.success();
    }

    @ApiOperation("设置菜品的状态")
    @PostMapping("/status/{status}")
    public Result startOrStop(Integer status, Long id){
        log.info("被删除的员工id：{}", id);
        dishService.startOrStop(status, id);

        return Result.success();
    }

    @ApiOperation("修改菜品状态")
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改的数据为： {}", dishDTO);
        dishService.update(dishDTO);

        return Result.success();
    }

}
