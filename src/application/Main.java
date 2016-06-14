package application;

import java.util.Random;
import java.util.Scanner;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;

public class Main extends Application {
	public static int MAX = 9999999;
	public static int INF = -9999999;

	public static int min(int a, int b) {
		return a < b ? a : b;
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			System.out.println("请输入大矩形的长和宽：");
			Scanner input = new Scanner(System.in);
			int height = input.nextInt();
			int width = input.nextInt();
			System.out.println("请输入随机生成的小矩形的个数：");
			int n = input.nextInt();
			System.out.println("随机生成" + n + "个小矩形");

			// 定义一个存储小矩形的数组
			SmallRectangle[] smallRectangle = new SmallRectangle[n];
			for (int i = 0; i < n; i++) {
				int[] chose = { height, width };
				// 设置一个数组随机选择长或宽
				int num = chose[new Random().nextInt(2)];
				// 没有这句会抛出空指针异常
				smallRectangle[i] = new SmallRectangle();
				// 随机生成的小矩形的长或宽最小值为2
				smallRectangle[i].setSmallheight((int) (Math.random() * (Math.random() + 1) * (num / 15) + 2));
				smallRectangle[i].setSmallwidth((int) (Math.random() * (Math.random() + 1) * (num / 15) + 2));
			}

			for (int i = 0; i < n; i++) {
				System.out.printf("第：%-6d", i + 1);
				System.out.printf("个矩形的长为：%-6d", smallRectangle[i].getSmallheight());
				System.out.printf(" " + "宽为：%-6d", smallRectangle[i].getSmallwidth());
				System.out.println();
			}
			input.close();

			// 将随机生成的矩形按从大到小排序
			for (int i = 0; i < n - 1; i++) {
				for (int j = 0; j < n - 1; j++) {
					if ((smallRectangle[j].getSmallheight()
							* smallRectangle[j].getSmallwidth()) < (smallRectangle[j + 1].getSmallheight()
									* smallRectangle[j + 1].getSmallwidth())) {
						// 交換
						int temp1 = smallRectangle[j].getSmallheight();
						int temp2 = smallRectangle[j].getSmallwidth();

						int temp3 = smallRectangle[j + 1].getSmallheight();
						int temp4 = smallRectangle[j + 1].getSmallwidth();

						smallRectangle[j + 1].setSmallheight(temp1);
						smallRectangle[j + 1].setSmallwidth(temp2);

						smallRectangle[j].setSmallheight(temp3);
						smallRectangle[j].setSmallwidth(temp4);
					}
				}
			}

			System.out.println("排序后的数组----------------------------------------------------------------------！");
			for (int i = 0; i < n; i++) {
				System.out.printf("第：%-6d", i + 1);
				System.out.printf("个矩形的长为：%-6d", smallRectangle[i].getSmallheight());
				System.out.printf(" " + "宽为：%-6d", smallRectangle[i].getSmallwidth());
				System.out.println();
			}

			// 设置一个大矩阵存储
			int[][] map = new int[height][width];
			// 设置当前大矩形中间为0 边框为1
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					if (i == 0 || j == 0 || i == height - 1 || j == width - 1) {
						map[i][j] = 1;// 1表示当前位置是边
					} else {
						map[i][j] = 0;// 0表示当前位置还没有放置矩形
					}
				}
			}

			int heighti = 0;
			int widthi = 0;
			int[][] Side1 = new int[height][width];// 当前小矩形的最大贴边数
			int[][] Side2 = new int[height][width];// 当前小矩形的最大贴边数
			double[][] C1 = new double[height][width];// 当前小矩形的最大穴度1
			double[][] C2 = new double[height][width];// 当前小矩形的最大穴度1

			// 当然点的坐标！
			int mapi = 0;
			int mapj = 0;

			boolean istrue = false;

			double maxC1 = INF;
			int maxSide1 = Side1[0][0];
			// 当C最大时的占角的点的坐标
			int maxi1 = 0;
			int maxj1 = 0;

			double maxC2 = INF;
			int maxSide2 = Side2[0][0];
			// 当C最大时的占角的点的坐标
			int maxi2 = 0;
			int maxj2 = 0;

			int maxi;
			int maxj;

			// 从第一个小矩形开始遍历
			for (int i = 0; i < n; i++) {
				istrue = false;
				// 改变一次长宽的位置
				for (int j = 0; j < 2; j++) {
					if (j == 0) {
						// 矩形框直接放进去
						heighti = smallRectangle[i].getSmallheight();
						widthi = smallRectangle[i].getSmallwidth();
					} else if (j == 1) {
						// 改变矩形框的长和宽从新放进去
						heighti = smallRectangle[i].getSmallwidth();
						widthi = smallRectangle[i].getSmallheight();
					}

					// 重新初始化穴度C数组和贴边数Side数组
					for (int k1 = 0; k1 < height; k1++) {
						for (int k2 = 0; k2 < width; k2++) {
							C1[k1][k2] = INF;
							C2[k1][k2] = INF;
							Side1[k1][k2] = 0;
							Side2[k1][k2] = 0;
						}
					}

					// 遍历整个map二维数组
					// mapi为纵坐标 mapj为横坐标
					for (mapi = 0; mapi < height; mapi++) {
						for (mapj = 0; mapj < width; mapj++) {
							// 此处有一个点在边框上
							if (map[mapi][mapj] == 1) {
								// 左上角
								try {
									boolean flag = true;
									if (map[mapi][mapj - 1] == 1 && map[mapi - 1][mapj] == 1) {
										// 在边框上的点有一个角
										outer: for (int i1 = mapi - 1; i1 > mapi - heighti; i1--) {
											for (int j1 = mapj - 1; j1 > mapj - widthi; j1--) {
												if (map[i1][j1] != 0) {
													flag = false;
													break outer;
												}
											}
										}

										// 如果占角成功
										// 计算当前情况下该矩形的穴度C和贴边数side
										if (flag == true) {
											istrue = true;
											// 矩形垂直方向上的最短d
											int dcurrent1 = 0;
											// 矩形水平方向上的最短d
											int dcurrent2 = 0;
											// 矩形的贴边数初始为2
											int side = 2;
											// d1 d2中更短的一个
											int dmin;
											int dcurrentmin1 = MAX;
											int dcurrentmin2 = MAX;
											// 橫遍历找最小d

											for (int i1 = mapi - 1; i1 > 0; i1--) {
												for (int j1 = mapj - widthi - 1; j1 >= 0; j1--) {
													if (map[i1][j1] == 1
															|| (map[i1][j1 - 1] == 1 && map[i1][j1] != 0)) {
														if (i1 >= mapi - heighti) {
															dcurrent1 = mapj - widthi - j1 + 1;

															if (dcurrent1 < dcurrentmin1) {
																dcurrentmin1 = dcurrent1;
															}
														} else {
															dcurrent1 = (mapj - widthi - j1 + 1)
																	+ (mapi - heighti - i1 + 1);

															if (dcurrent1 < dcurrentmin1) {
																dcurrentmin1 = dcurrent1;
															}
														}
													}
												}
											}

											// 竖遍历找最小d
											for (int j1 = mapj - 1; j1 > 0; j1--) {
												for (int i1 = mapi - heighti - 1; i1 >= 0; i1--) {
													if (map[i1][j1] == 1
															|| (map[i1 - 1][j1] == 1 && map[i1][j1] != 0)) {
														if (j1 <= mapj - widthi) {
															dcurrent2 = mapi - heighti - i1 + 1;

															if (dcurrent2 < dcurrentmin2) {
																dcurrentmin2 = dcurrent2;
															}
														} else {
															dcurrent2 = (mapi - heighti - i1 + 1)
																	+ (mapj - widthi - j1 + 1);

															if (dcurrent2 < dcurrentmin2) {
																dcurrentmin2 = dcurrent2;
															}
														}
													}
												}
											}

											if (dcurrentmin1 == 0) {
												side++;
											}
											if (dcurrentmin2 == 0) {
												side++;
											}

											dmin = min(dcurrentmin1, dcurrentmin2);
											// 计算穴度
											double c = 1 - (dmin / (Math.sqrt(heighti * widthi)));
											if (j == 0) {
												C1[mapi][mapj] = c;
												Side1[mapi][mapj] = side;
											} else if (j == 1) {
												C2[mapi][mapj] = c;
												Side2[mapi][mapj] = side;
											}
										}
									}
								} catch (Exception e) {
									// TODO: handle exception
								}

								// 左下角
								try {
									boolean flag = true;
									if (map[mapi][mapj - 1] == 1 && map[mapi + 1][mapj] == 1) {
										// 在边框上的点有一个角
										outer: for (int i1 = mapi + 1; i1 < mapi + heighti; i1++) {
											for (int j1 = mapj - 1; j1 > mapj - widthi; j1--) {
												if (map[i1][j1] != 0) {
													flag = false;
													break outer;
												}
											}
										}
										// 如果占角成功
										// 计算当前情况下该矩形的穴度c和贴边数side
										if (flag == true) {
											istrue = true;

											// 矩形垂直方向上的最短d
											int dcurrent1 = 0;
											// 矩形水平方向上的最短d
											int dcurrent2 = 0;
											// 矩形的贴边数初始为2
											int side = 2;
											// d1 d2中更短的一个
											int dmin;
											int dcurrentmin1 = MAX;
											int dcurrentmin2 = MAX;

											// 橫遍历找最小d
											for (int i1 = mapi + 1; i1 < height - 1; i1++) {
												for (int j1 = mapj - widthi - 1; j1 >= 0; j1--) {
													if (map[i1][j1] == 1
															|| (map[i1][j1 - 1] == 1 && map[i1][j1] != 0)) {
														if (i1 <= mapi + heighti) {
															dcurrent1 = mapj - widthi - j1 + 1;
															if (dcurrent1 < dcurrentmin1) {
																dcurrentmin1 = dcurrent1;
															}
														} else {
															dcurrent1 = (mapj - widthi - j1 + 1)
																	+ (i1 - heighti - mapi + 1);
															if (dcurrent1 < dcurrentmin1) {
																dcurrentmin1 = dcurrent1;
															}
														}
													}
												}
											}

											// 竖遍历找最小d
											for (int j1 = mapj - 1; j1 > 0; j1--) {
												for (int i1 = mapi + heighti + 1; i1 < height; i1++) {
													if (map[i1][j1] == 1
															|| (map[i1 + 1][j1] == 1 && map[i1][j1] != 0)) {
														if (j1 >= mapj - widthi) {
															dcurrent2 = i1 - mapi - heighti + 1;
															if (dcurrent2 < dcurrentmin2) {
																dcurrentmin2 = dcurrent2;
															}
														} else {
															dcurrent2 = (i1 - mapi - heighti + 1)
																	+ (mapj - widthi - j1 + 1);
															if (dcurrent2 < dcurrentmin2) {
																dcurrentmin2 = dcurrent2;
															}
														}
													}
												}
											}

											if (dcurrentmin1 == 0) {
												side++;
											}
											if (dcurrentmin2 == 0) {
												side++;
											}
											
											dmin = min(dcurrentmin1, dcurrentmin2);
											// 计算穴度
											double c = 1 - (dmin / (Math.sqrt(heighti * widthi)));
											if (j == 0) {
												C1[mapi][mapj] = c;
												Side1[mapi][mapj] = side;
											} else if (j == 1) {
												C2[mapi][mapj] = c;
												Side2[mapi][mapj] = side;
											}
										}
									}
								} catch (Exception e) {
									// TODO: handle exception
								}

								// 右上角
								try {
									boolean flag = true;
									if (map[mapi][mapj + 1] == 1 && map[mapi - 1][mapj] == 1) {
										// 在边框上的点有一个角
										outer: for (int i1 = mapi - 1; i1 > mapi - heighti; i1--) {
											for (int j1 = mapj + 1; j1 < mapj + widthi; j1++) {
												if (map[i1][j1] != 0) {
													flag = false;
													break outer;
												}
											}
										}

										// 如果占角成功
										// 计算当前情况下该矩形的穴度c和贴边数side
										if (flag == true) {
											istrue = true;
											// 矩形垂直方向上的最短d
											int dcurrent1 = 0;
											// 矩形水平方向上的最短d
											int dcurrent2 = 0;
											// 矩形的贴边数初始为2
											int side = 2;
											// d1 d2中更短的一个
											int dmin;
											int dcurrentmin1 = MAX;
											int dcurrentmin2 = MAX;
											// 橫遍历找最小d

											for (int i1 = mapi - 1; i1 > 0; i1--) {
												for (int j1 = mapj + widthi + 1; j1 < width; j1++) {
													if (map[i1][j1] == 1
															|| (map[i1][j1 + 1] == 1 && map[i1][j1] != 0)) {
														if (i1 >= mapi - heighti) {
															dcurrent1 = j1 - mapj - widthi + 1;

															if (dcurrent1 < dcurrentmin1) {
																dcurrentmin1 = dcurrent1;
															}
														} else {
															dcurrent1 = (j1 - mapj - widthi + 1)
																	+ (mapi - heighti - i1 + 1);

															if (dcurrent1 < dcurrentmin1) {
																dcurrentmin1 = dcurrent1;
															}
														}
													}
												}
											}

											// 竖遍历找最小d
											for (int j1 = mapj - 1; j1 > 0; j1--) {
												for (int i1 = mapi - heighti - 1; i1 >= 0; i1--) {
													if (map[i1][j1] == 1
															|| (map[i1 - 1][j1] == 1 && map[i1][j1] != 0)) {
														if (j1 <= mapj + widthi) {
															dcurrent2 = mapi - heighti - i1 + 1;

															if (dcurrent2 < dcurrentmin2) {
																dcurrentmin2 = dcurrent2;
															}
														} else {
															dcurrent2 = (mapi - heighti - i1 + 1)
																	+ (j1 - mapj - widthi + 1);

															if (dcurrent2 < dcurrentmin2) {
																dcurrentmin2 = dcurrent2;
															}
														}
													}
												}
											}

											if (dcurrentmin1 == 0) {
												side++;
											}
											if (dcurrentmin2 == 0) {
												side++;
											}

											dmin = min(dcurrentmin1, dcurrentmin2);
											// 计算穴度
											double c = 1 - (dmin / (Math.sqrt(heighti * widthi)));
											if (j == 0) {
												C1[mapi][mapj] = c;
												Side1[mapi][mapj] = side;
											} else if (j == 1) {
												C2[mapi][mapj] = c;
												Side2[mapi][mapj] = side;
											}
										}
									}
								} catch (Exception e) {
									// TODO: handle exception
								}

								// 右下角
								try {
									boolean flag = true;
									if (map[mapi][mapj + 1] == 1 && map[mapi + 1][mapj] == 1) {
										// 在边框上的点有一个角
										outer: for (int i1 = mapi + 1; i1 < mapi + heighti; i1++) {
											for (int j1 = mapj + 1; j1 < mapj + widthi; j1++) {
												if (map[i1][j1] != 0) {
													flag = false;
													break outer;
												}
											}
										}
										if (flag == true) {
											istrue = true;

											// 矩形垂直方向上的最短d
											int dcurrent1 = 0;
											// 矩形水平方向上的最短d
											int dcurrent2 = 0;
											// 矩形的贴边数初始为2
											int side = 2;
											// d1 d2中更短的一个
											int dmin;
											int dcurrentmin1 = MAX;
											int dcurrentmin2 = MAX;

											// 橫遍历找最小d
											for (int i1 = mapi + 1; i1 < height - 1; i1++) {
												for (int j1 = mapj + widthi + 1; j1 < width; j1++) {
													if (map[i1][j1] == 1
															|| (map[i1][j1 + 1] == 1 && map[i1][j1] != 0)) {
														if (i1 <= mapi + heighti) {
															dcurrent1 = j1 - mapj - widthi + 1;
															if (dcurrent1 < dcurrentmin1) {
																dcurrentmin1 = dcurrent1;
															}
														} else {
															dcurrent1 = (j1 - mapj - widthi + 1)
																	+ (i1 - heighti - mapi + 1);
															if (dcurrent1 < dcurrentmin1) {
																dcurrentmin1 = dcurrent1;
															}
														}
													}
												}
											}

											// 竖遍历找最小d
											for (int j1 = mapj + 1; j1 < width; j1++) {
												for (int i1 = mapi + heighti + 1; i1 < height; i1++) {
													if (map[i1][j1] == 1
															|| (map[i1 + 1][j1] == 1 && map[i1][j1] != 0)) {
														if (j1 <= mapj + widthi) {
															dcurrent2 = i1 - mapi - heighti + 1;
															if (dcurrent2 < dcurrentmin2) {
																dcurrentmin2 = dcurrent2;
															}
														} else {
															dcurrent2 = (i1 - mapi - heighti + 1)
																	+ (j1 - mapj - widthi + 1);
															if (dcurrent2 < dcurrentmin2) {
																dcurrentmin2 = dcurrent2;
															}
														}
													}
												}
											}

											if (dcurrentmin1 == 0) {
												side++;
											}
											if (dcurrentmin2 == 0) {
												side++;
											}

											dmin = min(dcurrentmin1, dcurrentmin2);
											// 计算穴度
											double c = 1 - (dmin / (Math.sqrt(heighti * widthi)));
											if (j == 0) {
												C1[mapi][mapj] = c;
												Side1[mapi][mapj] = side;
											} else if (j == 1) {
												C2[mapi][mapj] = c;
												Side2[mapi][mapj] = side;
											}
										}
									}
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						}
					}

					maxC1 = INF;
					maxC2 = INF;
					// 判断该矩形是否放入该大矩形
					if (istrue == true) {
						if (j == 0) {
							for (int i1 = 0; i1 < height; i1++) {
								for (int j1 = 0; j1 < width; j1++) {
									if (C1[i1][j1] > maxC1) {
										maxC1 = C1[i1][j1];
										maxSide1 = Side1[i1][j1];
										maxi1 = i1;
										maxj1 = j1;
									} else if (C1[i1][j1] == maxC1) {
										if (Side1[i1][j1] > maxSide1) {
											maxC1 = C1[i1][j1];
											maxSide1 = Side1[i1][j1];
											maxi1 = i1;
											maxj1 = j1;
										}
									}
								}
							}
						} else if (j == 1) {
							for (int i1 = 0; i1 < height; i1++) {
								for (int j1 = 0; j1 < width; j1++) {
									if (C2[i1][j1] > maxC2) {
										maxC2 = C2[i1][j1];
										maxSide2 = Side2[i1][j1];
										maxi2 = i1;
										maxj2 = j1;
									} else if (C2[i1][j1] == maxC2) {
										if (Side2[i1][j1] > maxSide2) {
											maxC2 = C1[i1][j1];
											maxSide2 = Side1[i1][j1];
											maxi2 = i1;
											maxj2 = j1;
										}
									}
								}
							}
						}

						int temp;
						if (maxC1 > maxC2) {
							maxi = maxi1;
							maxj = maxj1;
							// 交换
							temp = heighti;
							heighti = widthi;
							widthi = temp;
						} else {
							// 相同情况下就用j==1情况
							maxi = maxi2;
							maxj = maxj2;
						}

						if (j == 1) {
							try {
								// 左上角
								if ((map[maxi][maxj - 1] == 1) && (map[maxi - 1][maxj] == 1)) {
									boolean flag = true;
									// 在边框上的点有一个角
									outer: for (int i1 = maxi - 1; i1 > maxi - heighti; i1--) {
										for (int j1 = maxj - 1; j1 > maxj - widthi; j1--) {
											if (map[i1][j1] != 0) {
												flag = false;
												break outer;
											}
										}
									}
									if (flag == true) {
										for (int i1 = maxi; i1 > maxi - heighti; i1--) {
											for (int j1 = maxj; j1 > maxj - widthi; j1--) {
												if (i1 == maxi || i1 == maxi - heighti + 1 || j1 == maxj
														|| j1 == maxj - widthi + 1) {
													map[i1][j1] = 1;// 边框设置为1
												} else {
													map[i1][j1] = -1;// 边框里的设置为-1
												}
											}
										}
									}
								}
							} catch (Exception e) {
								// TODO: handle exception
							}

							// 左下角
							try {
								if ((map[maxi][maxj - 1] == 1) && (map[maxi + 1][maxj] == 1)) {
									boolean flag = true;
									// 在边框上的点有一个角
									outer: for (int i1 = maxi + 1; i1 < maxi + heighti; i1++) {
										for (int j1 = maxj - 1; j1 > maxj - widthi; j1--) {
											if (map[i1][j1] != 0) {
												flag = false;
												break outer;
											}
										}
									}
									if (flag == true) {
										for (int i1 = maxi; i1 < maxi + heighti; i1++) {
											for (int j1 = maxj; j1 > maxj - widthi; j1--) {
												if (i1 == maxi || i1 == maxi + heighti - 1 || j1 == maxj
														|| j1 == maxj - widthi + 1) {
													map[i1][j1] = 1;// 边框设置为1
												} else {
													map[i1][j1] = -1;// 边框里的设置为-1
												}
											}
										}
									}
								}
							} catch (Exception e) {
								// TODO: handle exception
							}
							// 右上角
							try {
								boolean flag = true;
								if (map[maxi][maxj + 1] == 1 && map[maxi - 1][maxj] == 1) {
									// 在边框上的点有一个角
									outer: for (int i1 = maxi - 1; i1 > maxi - heighti; i1--) {
										for (int j1 = maxj + 1; j1 < maxj + widthi; j1++) {
											if (map[i1][j1] != 0) {
												flag = false;
												break outer;
											}
										}
									}

									if (flag == true) {
										for (int i1 = maxi; i1 > maxi - heighti; i1--) {
											for (int j1 = maxj; j1 < maxj + widthi; j1++) {
												if (i1 == maxi || i1 == maxi - heighti + 1 || j1 == maxj
														|| j1 == maxj + widthi - 1) {
													map[i1][j1] = 1;// 边框设置为1
												} else {
													map[i1][j1] = -1;// 边框里的设置为-1
												}
											}
										}
									}
								}
							} catch (Exception e) {
								// TODO: handle exception
							}

							try {
								boolean flag = true;
								if (map[maxi][maxj + 1] == 1 && map[maxi + 1][maxj] == 1) {
									// 在边框上的点有一个角
									outer: for (int i1 = maxi + 1; i1 < maxi + heighti; i1++) {
										for (int j1 = maxj + 1; j1 < maxj + widthi; j1++) {
											if (map[i1][j1] != 0) {
												flag = false;
												break outer;
											}
										}
									}

									if (flag == true) {
										for (int i1 = maxi; i1 < maxi + heighti; i1++) {
											for (int j1 = maxj; j1 < maxj + widthi; j1++) {
												if (i1 == maxi || i1 == maxi + heighti - 1 || j1 == maxj
														|| j1 == maxj + widthi - 1) {
													map[i1][j1] = 1;// 边框设置为1
												} else {
													map[i1][j1] = -1;// 边框里的设置为-1
												}
											}
										}
									}
								}
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					}
				}

				int area = height * width;
				int currentarea = 0;
				double result = 0;

				if (istrue == true) {
					for (int p = 0; p < height; p++) {
						for (int q = 0; q < width; q++) {
							if (map[p][q] == 0) {
								currentarea++;
							}
						}
					}

					System.out.println("第" + (i + 1) + "个矩形");
					result = ((double) (area - currentarea) / (double) area);
					if (i == n - 1) {
						System.out.printf("最終比例 =%6.2f", result * 100);
						System.out.println("%");
					} else {
						System.out.printf("当前比例 =%6.2f", result * 100);
						System.out.println("%");
					}
				}
			}

			// 以下为javafx GUI代码

			GridPane pane = new GridPane();

			pane.setAlignment(Pos.CENTER);
			for (int p = 0; p < height; p++) {
				for (int q = 0; q < width; q++) {
					if (map[p][q] == 1) {
						// 描点
						pane.add(new Line(), p, q);
					}
				}
				System.out.println();
			}

			Scene scene = new Scene(pane, height, width);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
