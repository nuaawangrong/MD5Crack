# 实现进度记录

## 2021/12/12   21:25

1. 完成暴力破解。
2. 完成查表破解。
3. 完成彩虹表破解。

## 2021/12/16   11:23

1. 实现暴力破解

## TodoList

- [x] 生成暴力破解需要的MD5值文件

- [x] 生成彩虹表需要的文件

- [ ] 比较性能

  - [ ] 暴力破解

    - [ ] 纯数字字符串

      - [x] 1000

        - [x] 5位

        - [x] 6位

        - [x] 7位

        - [ ] 8位-------华为云进行中（时间太久，放弃）    

          开    始：Thu Dec 16 13:48:52 CST 2021  - 1/100

          阶段一：Thu Dec 16 17:42:01 CST 2021  - 114/1000

          阶段二：Thu Dec 16 18:33:10 CST 2021  - 142/1000

          

      - [ ] 10000

        - [x] 5位       华为云上

        - [x] 6位

        - [ ] 7位------华为云进行中 -----预计17日晚上完成

          开    始：Thu Dec 16 13:10:52 CST 2021  -  1/1000

          阶段一：Thu Dec 16 17:44:28 CST 2021  -  1488/10000

          阶段二：Thu Dec 16 18:31:46 CST 2021  -  1775/10000

          阶段三：Thu Dec 16 23:47:36 CST 2021  -  3603/10000

  - [ ] 查表破解

    - [ ] 纯数字字符串
      - [ ] 1000
        - [x] 5位
        - [x] 6位
        - [x] 7位
      - [ ] 10000
        - [x] 5位
        - [x] 6位
        - [x] 7位

  - [ ] 彩虹表破解

    - [ ] 纯数字字符串
      - [ ] 1000
        - [ ] 5位-------------阿里云在跑
        - [ ] 6位-------------华为云再跑
        - [ ] 7位
      - [ ] 10000
        - [ ] 5位
        - [ ] 6位
        - [ ] 7位

- [x] 完成暴力破解。

- [x] 完成查表破解。

- [x] 完成彩虹表破解。

- [x] 实现查表破解函数，一个文件实现所有的测试用例

- [ ] 实现暴力破解函数，一个文件实现所有的测试用例

- [ ] 实现彩虹表破解函数，一个文件实现所有的测试用例

  

  | `chainLength` | `chainAmountAmount` |
  | ------------- | ------------------- |
  | 5             | passwordAmount/2    |
  | 10            | passwordAmount/2    |
  | 50            | passwordAmount/2    |
  | 10            | passwordAmount/10   |
  | 50            | passwordAmount/10   |
  | 100           | passwordAmount/100  |

  



## 性能比较思路

每次随机抽取`100`个HASH值，通过比较`存储文件的大小`、`平均破解时间`两个指标（彩虹表可能有些不能破解）。

1. 分别用不同长度的纯数字字符串比较：5、6、7位,测试数据用量分别为1000,10000。


彩虹表中又可以对`chainLength`、`chainAmountAmount`来进行比较。









生成5位时在实体机上卡死了，后面修改程序，不使用set存储String password的值。

生成混6位有小写字母和数字的字符串的MD5值时，云服务器都会卡死，取消实现。

![image-20211213095701178](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20211213095701178.png)

时间不够，去掉混有小写字母对的对比。