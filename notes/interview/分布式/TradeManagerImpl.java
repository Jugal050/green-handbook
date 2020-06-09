@Service
public TradeManagerImpl implements TradeManager {

	private static final Logger logger = LoggerFactory.getLogger(TradeManagerImpl.class);

	@Value("${trade.cache.expireTime}")
	private Long expireTime;

	@Autowired
	private TradeService tradeService;

	@Autowired
	private CacheManager redisCacheManager;

	/**
	 * 定时（每分钟）/线程（每秒）扫描的更新交易状态公共方法
	 */
	public void updateStatus() {

		// 全表扫描，查询需要更新状态的交易记录
		List<Trade> tradeList = tradeService.selectTradeList();

		// 遍历更新记录
		for (Trade trade : tradeList) {
			logger.info();
			try {
				// 查询缓存（该步骤只做简单过滤，并不能严格保证后续是否有多个线程处理同一条记录）
				Object cache = redisCacheManager.getCache(trade.getTradeId());
				// 如果缓存不为空，表示有已有线程在处理，跳过该记录继续下一条
				if (!Objects.equals(null, cache)) {
					continue;
				}
				// 如果缓存为空，则添加缓存
				redisCacheManager.addCache(trade.getTradeId(), System.currentTimeMillis(), expireTime);

				// 更新状态，推送工作流
				tradeService.updateStatus(trade);

			} catch (Throwable e) {
				logger.error("--- update status exception. ---", e);
				// 邮件通知
			} finally {
				redisCacheManager.removeCache(trade.getTradeId());
			}
		}

	}
}

@Service
public TradeServiceImpl implements TradeService {

	private static final Logger logger = LoggerFactory.getLogger(TradeServiceImpl.class);

	@Autowired
	private TradeMapper tradeMapper;

	/**
	 * 更新交易状态
	 */
	public void updateStatus(Trade trade) throw Exception {	


		// 修改状态，同时获取数据库update行级锁，锁住该记录（使用数据库行级锁，做并发控制，保证只有一个线程处理）
		Trade temp = new Trade();
		temp.setTradeId(trade.getTradeId());
		temp.setStatus("xxStatus");
		int row = tradeMapper.updateStatusByPrimaryKey(temp); 
		// row == 0，表示已有线程正在处理且锁定该记录
		if (row == 0) {
			logger.info("--- update rows count is 0 ---");
			return;
		}

		// 其他流程

	}

}