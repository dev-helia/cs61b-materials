# 📝 2048 游戏 README 文档

🚀 **作者：Helia**  
📌 **项目概述**  
本项目是 **cs61b-project0-2048 游戏** 的 Java 实现，核心逻辑包括 **棋盘管理、移动合并、游戏结束判断** 等。 本人在做项目作业过程中的一些坑点和雷点在此记录.

# 项目目标
项目除了本身的skeleton代码外,需要学习者实现:

| 方法                              | 作用         | 逻辑                                |
| ------------------------------- | ---------- | --------------------------------- |
| `emptySpaceExists(Board b)`     | 检查是否有空格子   | 遍历棋盘，检查是否有 `null`                 |
| `maxTileExists(Board b)`        | 检查是否有 2048 | 遍历棋盘，检查是否有 `tile.value() == 2048` |
| `atLeastOneMoveExists(Board b)` | 检查是否还能移动   | 看有无空格 or 相邻 tile 是否相等             |
| `tilt(Side side)`               | 执行滑动逻辑     | 移动 tile，合并相同的，更新棋盘                |


---
# 提示
## 📌 1. ⚠️**棋盘坐标系解析**
在 **2048 游戏** 里，棋盘的坐标系统是 **(col, row)** 形式，而 **不是** 常见的 **(row, col)** ❗

✅ **棋盘的左下角是 (0,0)**，右上角是 **(size-1, size-1)**。  
✅ **Y 轴（行 row）是从下往上增加**，即 **row = 0** 是最底层，**row = size-1** 是最上层！  
✅ **X 轴（列 col）是从左往右增加**，即 **col = 0** 是最左边，**col = size-1** 是最右边！

📌 **示意图**（假设 size = 4）：
```
(0,3)  (1,3)  (2,3)  (3,3)  ← row = 3 (最上层)
(0,2)  (1,2)  (2,2)  (3,2)  
(0,1)  (1,1)  (2,1)  (3,1)  
(0,0)  (1,0)  (2,0)  (3,0)  ← row = 0 (最底层)
   ↑      ↑      ↑      ↑   
  col=0  col=1  col=2  col=3
```
## 📌 2. **依赖包问题**
找不到 StdDraw问题:需要重新添加
🚀 下载 StdDraw.java
打开[官方网站](https://introcs.cs.princeton.edu/java/stdlib/StdDraw.java.html)
右键点击页面中的 StdDraw.java 代码，选择 "保存为"。
文件名： StdDraw.java
保存到你的 javalib/ 文件夹
并在 IntelliJ，然后点 File -> Project Structure（⌘ + ;）左边选择 Libraries添加依赖

## 📌 3. **方法版本问题**
✅使用 Java 8 兼容方法
👉 把 getMenuShortcutKeyMaskEx() 改成 getMenuShortcutKeyMask()
📌 这应该就能解决问题，因为 getMenuShortcutKeyMask() 是 Java 8 支持的，但 Java 9 之后被 getMenuShortcutKeyMaskEx() 取代了。
---

# 🎯 2. **游戏逻辑解析**

### **1️⃣ `emptySpaceExists(Board b)`**
**问题**：2048 里的棋盘上**是否有空格子**？  
**思考方式**：
- 遍历整个棋盘，看看有没有 `null` 的格子，如果有，就返回 `true`。
- 没有空格子，返回 `false`。

---

### **2️⃣ `maxTileExists(Board b)`**
**问题**：棋盘上是否有 2048？  
**思考方式**：
- 遍历棋盘，看看是否有 `tile.value() == 2048`，如果有，就返回 `true`。
- 没有 2048，就返回 `false`。

---

### **3️⃣ `atLeastOneMoveExists(Board b)`**
**问题**：棋盘上是否还能进行任何操作？  
**思考方式**：
- **第一种可能性**：有空格子？ → **调用 `emptySpaceExists`**，如果有空格，就返回 `true`。
- **第二种可能性**：有没有**两个相邻的格子是一样的**？
    - 遍历棋盘，看看水平方向和垂直方向有没有两个相邻的 tile 值相等。
    - 如果有，返回 `true`，否则返回 `false`。

---

### **4️⃣ `tilt(Side side)

1. **转换视角**（setViewingPerspective）
    - **先把目标方向变为“向上”处理**，再复原回来 ❗
    - 例如：如果要向右移动，就 **先旋转棋盘**，让它变成 **“向上”** 再操作。

2. **遍历所有列（col），逐行检查是否需要合并/移动**
    - 需要从 **倒数第二行（size-2）开始遍历**，因为最上面的格子不需要动。
    - **遍历方向是从下往上**（row 递减）❗

3. **寻找 tile 能移动到的最高位置**
    - **使用 destRow 变量** 来找到 tile 可能的移动目标位置：
      ```java
      int destRow = row;
      while (destRow + 1 < size && board.tile(col, destRow + 1) == null) {
          destRow++;
      }
      ```
    - **注意**：`destRow + 1 < size` 这个条件保证不会越界。

4. **执行合并**
    - 如果 tile **上面紧挨的块**（destRow+1）和它数值相同，且没有合并过，则进行合并：
      ```java
      if (destRow + 1 < size && board.tile(col, destRow + 1).value() == tile.value() && !merged[destRow + 1]) {
          board.move(col, destRow + 1, tile);
          score += tile.value() * 2;  // 累加分数
          merged[destRow + 1] = true; // 标记已合并，防止连续合并
          changed = true;
      }
      ```

5. **执行普通移动**
    - 如果 **找到了空位**（destRow 变了），则移动 tile：
      ```java
      else if (destRow != row) {
          board.move(col, destRow, tile);
          changed = true;
      }
      ```

6. **复原视角**
    - **因为之前旋转了棋盘，所以操作完毕后一定要复原！**
      ```java
      board.setViewingPerspective(Side.NORTH);
      ```

---

