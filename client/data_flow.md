#### 客户端与服务端交互数据流

1. 上传wifi信号信息

| 字段 | 取值 |描述 |
| :--|: ---|:----|
| postion | [0.2345, 0.2167]| 在该地图上的位置(x,y)，此值为相对位置，为0-1之间小数，保留四位小数 |
| buildingId | （由服务端获取） |建筑物ID |
| floor | B1，F1，F2| 楼层 |
| timestamp | 1432023794 | 时间戳 |
| direction | 0 - 360 | 朝向，以正北方向顺时针旋转的角度 |
| rssis | mac:rssi| 信号强度列表 |

示例：
```
    {
	"postion":[0.2345, 0.2167],
	"buildingId": "building_123",
	"floor": "1",
	"timestamp": 1432023794,
	"direction": 90,
	"rssis": [
		{
		 "mac": "45:C3:B5:23:78:DD:45",
		 "rssi": "-45"
		},
		{
		 "mac": "35:C3:B5:23:78:DD:45",
		 "rssi": "-46"
		},
		{
		 "mac": "25:C3:B5:23:78:DD:45",
		 "rssi": "-44"
		},
		{
		 "mac": "15:C3:B5:23:78:DD:45",
		 "rssi": "-56"
		}
	]
}
```

2.请求定位信息

| 字段 | 取值 | 描述 |
|:-----|:----|:---- |
| buildingId | （由服务端获取） |建筑物ID |
| floor | B1，F1，F2| 楼层 |
| timestamp | 1432023794 | 时间戳 |
| direction | 0 - 360 | 朝向，以正北方向顺时针旋转的角度 |
| rssis | mac:rssi| 信号强度列表 |

示例：

```
{
	"buildingId": "building_123",
	"floor": "1"
	"timestamp": 1432023794,
	"direction": 90,
	"rssis": [
		{
		 "mac": "45:C3:B5:23:78:DD:45",
		 "rssi": "-45"
		},
		{
		 "mac": "35:C3:B5:23:78:DD:45",
		 "rssi": "-46"
		},
		{
		 "mac": "25:C3:B5:23:78:DD:45",
		 "rssi": "-44"
		},
		{
		 "mac": "15:C3:B5:23:78:DD:45",
		 "rssi": "-56"
		}
	]
}
```