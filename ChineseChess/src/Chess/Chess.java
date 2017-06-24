package Chess;

import java.util.Random;

public class Chess 
{
	static final int hashEXACT=0;
	static final int hashALPHA=1;
	static final int hashBETA=2;
	Move g_KillerMove[][]=new Move[256][2];       // 杀手着法表
	char g_History[]=new char[65536];  // 历史表
	int WinValue = 25500;
	char PositionMask[] = {2, 4, 16, 1, 1, 1, 8};
	char LegalPosition[][] ={
			{
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 9, 9, 9, 9, 9, 9, 9, 9, 9, 0, 0, 0, 0,
				0, 0, 0, 9, 9, 9, 9, 9, 9, 9, 9, 9, 0, 0, 0, 0,
				0, 0, 0, 9, 9, 9, 9, 9, 9, 9, 9, 9, 0, 0, 0, 0,
				0, 0, 0, 9, 9, 9, 9, 9, 9, 9, 9, 9, 0, 0, 0, 0,
				0, 0, 0, 9, 9, 9, 9, 9, 9, 9, 9, 9, 0, 0, 0, 0,
				0, 0, 0, 9, 1,25, 1, 9, 1,25, 1, 9, 0, 0, 0, 0,
				0, 0, 0, 9, 1, 9, 1, 9, 1, 9, 1, 9, 0, 0, 0, 0,
				0, 0, 0, 17, 1, 1, 7, 19, 7, 1, 1, 17, 0, 0, 0, 0,
				0, 0, 0, 1, 1, 1, 3, 7, 3, 1, 1, 1, 0, 0, 0, 0,
				0, 0, 0, 1, 1, 17, 7, 3, 7, 17, 1, 1, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
			},
			{
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 1, 1, 17, 7, 3, 7, 17, 1, 1, 0, 0, 0, 0,
				0, 0, 0, 1, 1, 1, 3, 7, 3, 1, 1, 1, 0, 0, 0, 0,
				0, 0, 0, 17, 1, 1, 7, 19, 7, 1, 1, 17, 0, 0, 0, 0,
				0, 0, 0, 9, 1, 9, 1, 9, 1, 9, 1, 9, 0, 0, 0, 0,
				0, 0, 0, 9, 1,25, 1, 9, 1,25, 1, 9, 0, 0, 0, 0,
				0, 0, 0, 9, 9, 9, 9, 9, 9, 9, 9, 9, 0, 0, 0, 0,
				0, 0, 0, 9, 9, 9, 9, 9, 9, 9, 9, 9, 0, 0, 0, 0,
				0, 0, 0, 9, 9, 9, 9, 9, 9, 9, 9, 9, 0, 0, 0, 0,
				0, 0, 0, 9, 9, 9, 9, 9, 9, 9, 9, 9, 0, 0, 0, 0,
				0, 0, 0, 9, 9, 9, 9, 9, 9, 9, 9, 9, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
			}
		};
	HashNode hashTable[]=new HashNode[1024*1024];
	int NOVALUE=9999;
	long ZobristPlayer;//32位走棋方键值
	long ZobristPlayerCheck;//64位走棋方校验值
	long ZobristTable[][]=new long[14][256];//32位的棋子在棋盘各位置的键值
	long ZobristTableCheck[][]=new long[14][256];//64位棋子在棋盘个位置的校验值
	long ZobristKey;
	long ZobristKeyCheck;
	long HashMask;
	//moveArray的数目
	int moveArrayNum;
	//side 当前下棋方 如果是0,那么是红方下棋,如果是1,那么是黑方下棋
	int side;
	//moevNum 为保存的合理走法的数量
	int moveNum;
	//走法栈
	Move moveStack[]=new Move[128];
	//栈顶指针
	int stackTop;
	//当前的最佳走法
	static Move bestMove=new Move();
	//当前搜索深度
	int ply;
	//最大搜索深度
	int MaxDepth;
	//最小value值
	int maxAlpha;
	//最大beta值
	int maxBeta;
	// MVV价值表，是按照帅(将)=8、车=6、马炮=4、其他=2设定的
	int MvvValues[] = {
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  8, 2, 2, 2, 2, 4, 4, 6, 6, 4, 4, 2, 2, 2, 2, 2,
	  8, 2, 2, 2, 2, 4, 4, 6, 6, 4, 4, 2, 2, 2, 2, 2,
	};
	//边界数组
	int []border=
		{
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,24,24,28,27,25,27,28,24,24,0,0,0,0,
				0,0,0,24,24,24,25,27,25,24,24,24,0,0,0,0,
				0,0,0,28,24,24,27,29,27,24,24,28,0,0,0,0,
				0,0,0,56,24,56,24,56,24,56,24,56,0,0,0,0,
				0,0,0,56,24,60,24,56,24,60,24,56,0,0,0,0,
				0,0,0,56,40,60,40,56,40,60,40,56,0,0,0,0,
				0,0,0,56,40,56,40,56,40,56,40,56,0,0,0,0,
				0,0,0,44,40,40,43,45,43,40,40,44,0,0,0,0,
				0,0,0,40,40,40,41,43,41,40,40,40,0,0,0,0,
				0,0,0,40,40,44,43,41,43,44,40,40,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		};
	
	//棋子位置分值
	int []PieceNumToType=
		{
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,1,1,2,2,3,3,4,4,5,5,6,6,6,6,6,
				0,1,1,2,2,3,3,4,4,5,5,6,6,6,6,6,
		};
	
	int [][][]PositionValue=
		{
				//黑方
				{
					{//红 帅
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,0,0,0,15,20,15,0,0,0,0,0,0,0,
						0,0,0,0,0,0,10,10,10,0,0,0,0,0,0,0,
						0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						//楚河汉界
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//红士
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,0,0,0,30,0,30,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,22,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,30,0,30,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						//楚河汉界
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//红象
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,	0,0,30,0,0,0,30,0,0,		0,0,0,0,
						0,0,0,	0,0,0,0,0,0,0,0,0,			0,0,0,0,
						0,0,0,	20,0,0,0,35,0,0,0,20,		0,0,0,0,
						0,0,0,	0,0,0,0,0,0,0,0,0,			0,0,0,0,
						0,0,0,	0,0,25,0,0,0,25,0,0,		0,0,0,0,

						//楚河汉界
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//红马
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,		60,70,75,70,60,70,75,70,60,				0,0,0,0,
						0,0,0,		70,75,75,70,50,70,75,75,70,				0,0,0,0,
						0,0,0,		80,80,90,90,80,90,90,80,80,				0,0,0,0,
						0,0,0,		80,90,100,100,90,100,100,90,80,			0,0,0,0,
						0,0,0,		90,100,100,110,100,110,100,100,90,		0,0,0,0,
						//楚河汉界
						0,0,0,		90,110,110,120,100,120,110,110,90,		0,0,0,0,
						0,0,0,		90,100,120,130,110,130,120,100,90,		0,0,0,0,
						0,0,0,		90,100,120,125,120,125,120,100,90,		0,0,0,0,
						0,0,0,		80,110,125,90,70,90,125,110,80,			0,0,0,0,
						0,0,0,		70,80,90,80,70,80,90,80,70,				0,0,0,0,

						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//红车
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,		150,160,150,160,150,160,150,160,150,	0,0,0,0,
						0,0,0,		160,170,160,160,150,160,160,170,160,	0,0,0,0,
						0,0,0,		170,180,170,170,160,170,170,180,170,	0,0,0,0,
						0,0,0,		170,190,180,220,240,220,200,190,170,	0,0,0,0,
						0,0,0,		180,220,210,240,250,240,210,220,180,	0,0,0,0,
						//楚河汉界
						0,0,0,		180,220,210,240,250,240,210,220,180,	0,0,0,0,
						0,0,0,		180,220,210,240,250,240,210,220,180,	0,0,0,0,
						0,0,0,		170,190,200,220,240,220,200,190,170,	0,0,0,0,
						0,0,0,		170,180,170,190,250,190,170,180,170,	0,0,0,0,
						0,0,0,		160,170,160,150,150,150,160,170,160,	0,0,0,0,

						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//红炮
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,		80,90,80,70,60,70,80,90,80,			0,0,0,0,
						0,0,0,		80,90,80,70,65,70,80,90,80,			0,0,0,0,
						0,0,0,		90,100,80,80,70,80,80,100,90,		0,0,0,0,
						0,0,0,		90,100,90,90,110,90,90,100,90,		0,0,0,0,
						0,0,0,		90,100,90,110,130,110,90,100,90,	0,0,0,0,
						//楚河汉界
						0,0,0,		90,110,90,110,130,110,90,110,90,	0,0,0,0,
						0,0,0,		90,110,90,110,130,110,90,110,90,	0,0,0,0,
						0,0,0,		100,120,90,80,80,80,90,120,100,		0,0,0,0,
						0,0,0,		110,125,100,70,60,70,100,125,110,	0,0,0,0,
						0,0,0,		125,130,100,70,60,70,100,130,125,	0,0,0,0,

						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//红兵
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,		10,0,15,0,15,0,15,0,10,			0,0,0,0,
						0,0,0,		10,0,15,0,15,0,15,0,10,			0,0,0,0,
						//楚河汉界
						0,0,0,		15,20,20,20,20,20,20,20,15,		0,0,0,0,
						0,0,0,		20,25,25,30,30,30,25,25,20,		0,0,0,0,
						0,0,0,		25,30,30,40,40,40,30,20,25,		0,0,0,0,
						0,0,0,		25,30,40,50,60,50,40,30,25,		0,0,0,0,
						0,0,0,		10,10,10,20,25,20,10,10,10,		0,0,0,0,

						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
				},
				//黑方
				{
					{//黑帅
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						//楚河汉界
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,
						0,0,0,0,0,0,10,10,10,0,0,0,0,0,0,0,
						0,0,0,0,0,0,15,20,15,0,0,0,0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//黑士
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						//楚河汉界
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,30,0,30,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,22,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,30,0,30,0,0,0,0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//黑象
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						//楚河汉界
						0,0,0,	0,0,25,0,0,0,25,0,0,		0,0,0,0,
						0,0,0,	0,0,0,0,0,0,0,0,0,			0,0,0,0,
						0,0,0,	20,0,0,0,35,0,0,0,20,		0,0,0,0,
						0,0,0,	0,0,0,0,0,0,0,0,0,			0,0,0,0,
						0,0,0,	0,0,30,0,0,0,30,0,0,		0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//黑马
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,		70,80,90,80,70,80,90,80,70,				0,0,0,0,
						0,0,0,		80,110,125,90,70,90,125,110,80,			0,0,0,0,
						0,0,0,		90,100,120,125,120,125,120,100,90,		0,0,0,0,
						0,0,0,		90,100,120,130,110,130,120,100,90,		0,0,0,0,
						0,0,0,		90,110,110,120,100,120,110,110,90,		0,0,0,0,
						//楚河汉界
						0,0,0,		90,100,100,110,100,110,100,100,90,		0,0,0,0,
						0,0,0,		80,90,100,100,90,100,100,90,80,			0,0,0,0,
						0,0,0,		80,80,90,90,80,90,90,80,80,				0,0,0,0,
						0,0,0,		70,75,75,70,50,70,75,75,70,				0,0,0,0,
						0,0,0,		60,70,75,70,60,70,75,70,60,				0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//黑车
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,		160,170,160,150,150,150,160,170,160,	0,0,0,0,
						0,0,0,		170,180,170,190,250,190,170,180,170,	0,0,0,0,
						0,0,0,		170,190,200,220,240,220,200,190,170,	0,0,0,0,
						0,0,0,		180,220,210,240,250,240,210,220,180,	0,0,0,0,
						0,0,0,		180,220,210,240,250,240,210,220,180,	0,0,0,0,
						//楚河汉界
						0,0,0,		180,220,210,240,250,240,210,220,180,	0,0,0,0,
						0,0,0,		170,190,180,220,240,220,200,190,170,	0,0,0,0,
						0,0,0,		170,180,170,170,160,170,170,180,170,	0,0,0,0,
						0,0,0,		160,170,160,160,150,160,160,170,160,	0,0,0,0,
						0,0,0,		150,160,150,160,150,160,150,160,150,	0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//黑炮
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,		125,130,100,70,60,70,100,130,125,	0,0,0,0,
						0,0,0,		110,125,100,70,60,70,100,125,110,	0,0,0,0,
						0,0,0,		100,120,90,80,80,80,90,120,100,		0,0,0,0,
						0,0,0,		90,110,90,110,130,110,90,110,90,	0,0,0,0,
						0,0,0,		90,110,90,110,130,110,90,110,90,	0,0,0,0,
						//楚河汉界
						0,0,0,		90,100,90,110,130,110,90,100,90,	0,0,0,0,
						0,0,0,		90,100,90,90,110,90,90,100,90,		0,0,0,0,
						0,0,0,		90,100,80,80,70,80,80,100,90,		0,0,0,0,
						0,0,0,		80,90,80,70,65,70,80,90,80,			0,0,0,0,
						0,0,0,		80,90,80,70,60,70,80,90,80,			0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//黑兵
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,		10,10,10,20,25,20,10,10,10,		0,0,0,0,
						0,0,0,		25,30,40,50,60,50,40,30,25,		0,0,0,0,
						0,0,0,		25,30,30,40,40,40,30,20,25,		0,0,0,0,
						0,0,0,		20,25,25,30,30,30,25,25,20,		0,0,0,0,
						0,0,0,		15,20,20,20,20,20,20,20,15,		0,0,0,0,
						//楚河汉界
						0,0,0,		10,0,15,0,15,0,15,0,10,			0,0,0,0,
						0,0,0,		10,0,15,0,15,0,15,0,10,			0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					}
				}
		};
	
	int []board=new int [256];	//整个大棋盘数组
	
	//棋子数组,一维保存棋子是否存在及其位置,二维保存棋子的价值
	int [][]piece=
		{
			{
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				199,198,200,197,201,196,202,195,203,164,170,147,149,151,153,155,55,54,56,53,57,52,58,51,59,84,90,99,101,103,105,107
			},
			{
					0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					10000,200,200,200,200,400,400,900,900,450,450,100,100,100,100,100,100,10000,200,200,200,200,400,400,900,900,450,450,100,100,100,100,100,100
			},
		};
	
	//移动方向按照左上角,顺时针进行
	int [] king={-16,1,16,-1};	//将,车,炮 移动数组
	int [] knight={-17,-15,17,15};//士  移动数组
	int [] elephant={-34,-30,34,30};//象移动数组
	int [] horse={-18,-33,-31,-14,18,33,31,14};//马移动数组
	int [][] soldier={{-16,1,-1},{16,1,-1}}; //兵的移动数组
	
	int [] horseFeet={-1,-16,-16,1,1,16,16,-1};//马脚位置
	int [] elephantFeet={-17,-15,17,15};//象脚位置
	
	//fen值
	static String fen="";

	public void changeSide()
	{
		side = 1- side;
		ZobristKey ^= ZobristPlayer;
		ZobristKeyCheck ^= ZobristPlayerCheck;
	}
	public Chess()
	{
		side=0;
		moveNum=0;
		stackTop=0;
		maxAlpha=-30000;
		maxBeta=30000;
		MaxDepth=3;
		ply=0;
		moveArrayNum=0;
		NewHashTable();
	}
	
	public static void main(String [] args)
	{
		Chess chess =new Chess();
		Move mm1=new Move();
		chess.initBoard();
		//chess.printf1();
		fen="5abn1/3rnkcc1/4ba3/6P2/9/9/5p3/3p2p2/8r/2p2K2p w";
		chess.fenToArray();
		chess.printf1();
		System.out.println("\n");
		chess.computerThink();
		chess.makeMove(bestMove);
		chess.printf1();
		chess.legalMove(mm1);
	}
	
	public void printf1()
	{
		for(int i=0;i<256;i++)
		{
			System.out.printf("%d\t",board[i]);
			if((i+1)%16==0)
				System.out.printf("\n");
		}
	}
	
	public void initBoard()	//初始棋盘,棋子数组
	{
		for(int i=0;i<256;i++)
		{
			board[i]=0;
		}
		for(int i=16;i<48;i++)
		{
			board[piece[0][i]]=i;
		}
	}
	
	public void clearBoard()//清除棋盘,棋子数组
	{
		for(int i=0;i<256;i++)
		{
			board[i]=0;
		}
		for(int j=0;j<48;j++)
		{
			piece[0][j]=0;
		}
		ZobristKey= 0;
		ZobristKeyCheck= 0;
	}
	
	//将fen中char类型的值转化为int类型
	public int charToInt(char m)
	{
		switch(m)
		{
		case 'K':
		case 'k':return 0;
		case 'A':
		case 'a':return 1;
		case 'B':
		case 'b':return 2;
		case 'N':
		case 'n':return 3;
		case 'R':
		case 'r':return 4;
		case 'C':
		case 'c':return 5;
		case 'P':
		case 'p':return 6;
		}
		return 8;
	}
	
	//将棋盘数组中int类型的值转化为fen中的char类型
	public char intToChar(int m)
		{
			switch(m)
			{
			case 16:return 'K';
			case 17:
			case 18:return 'A';
			case 19:
			case 20:return 'B';
			case 21:
			case 22:return 'N';
			case 23:
			case 24:return 'R';
			case 25:
			case 26:return 'C';
			case 27:
			case 28:
			case 29:
			case 30:
			case 31:return 'P';
			case 32:return 'k';
			case 33:
			case 34:return 'a';
			case 35:
			case 36:return 'b';
			case 37:
			case 38:return 'b';
			case 39:
			case 40:return 'r';
			case 41:
			case 42:return 'c';
			case 43:
			case 44:
			case 45:
			case 46:
			case 47:return 'P';
			}
			return 8;
		}
	
	public void fenToArray() 	//Fen转化为棋盘数组
	{
		int i,j,k;
		int pt;
		int a=0;
		int pcWhite[]={16,17,19,21,23,25,27};
		int pcBlack[]={32,33,35,37,39,41,43};
		clearBoard();
		if(fen.charAt(a)=='\0')
		{
			return;
		}
		
		i=3;
		j=3;
		while(fen.charAt(a)!=' ')
		{
			if(fen.charAt(a)=='/')
			{
				i++;
				j=3;
				
			}
			else if(fen.charAt(a)>='1'&&fen.charAt(a)<='9')
			{
				for(k=0;k<(int)(fen.charAt(a)-'0');k++)
				{
					if(j>=11)
						break;
					j++;
				}
			}
			else if(fen.charAt(a)>='A'&&fen.charAt(a)<='Z')
			{
				k=charToInt(fen.charAt(a));
				if(k<7)
				{
					board[(i<<4)+j]=pcWhite[k];
					piece[0][pcWhite[k]]=(i<<4)+j;
					pt=PieceNumToType[pcWhite[k]];
					ZobristKey ^= ZobristTable[pt][(i<<4)+j];
					ZobristKeyCheck ^= ZobristTableCheck[pt][(i<<4)+j];
					pcWhite[k]++;
				}
				j++;
			}
			else if(fen.charAt(a)>='a'&&fen.charAt(a)<='z')
			{
				k=charToInt(fen.charAt(a));
				if(k<7)
				{
					board[(i<<4)+j]=pcBlack[k];
					piece[0][pcWhite[k]]=(i<<4)+j;
					pt=PieceNumToType[pcWhite[k]];
					ZobristKey ^= ZobristTable[pt][(i<<4)+j];
					ZobristKeyCheck ^= ZobristTableCheck[pt][(i<<4)+j];
					pcBlack[k]++;
				}
				j++;
			}
			a++;
		}
		a++;
		if((int)fen.charAt(a)=='w')
			side=0;
		else 
			side=1;
	}
	public void intToFen() //棋盘数组转化为Fen值
	{
		int i,j,k;
		char m;
		fen="";
		i=3;
		j=3;
		k=0;
		
		for(;i<12;i++)
		{
			for(;j<=11;j++)
			{
				if(board[(i<<4)+j]==0)
				{
					k++;
				}
				else if(board[(i<<4)+j]!=0)
				{
					if(k!=0)
					{
						fen=fen+k;
						k=0;
					}
					else
					{
						m=intToChar(board[(i<<4)+j]);
						fen=fen+m;
					}
				}
			}
		}
		fen=fen+" ";
		if(side==0)
		{
			fen=fen+"w";
		}
		else 
			fen=fen+"b";
	}
	
	public void saveMove(int from,int to,Move mv[])//保存移动数组
	{
		int p;
		p=board[to];
		piece[0][board[from]]=to;
		if(p!=0)
			piece[0][p]=0;
		board[to]=board[from];
		board[from]=0;
		
		int r=Check(side);
		board[from]=board[to];
		board[to]=p;
		piece[0][board[from]]=from;
		if(p!=0)
			piece[0][p]=to;
			if(r==0)
			{
				mv[moveNum]=new Move();
				mv[moveNum].from=from;
				mv[moveNum].to=to;
				moveNum++;
			}
	}
	public void saveMove(int from,int to,Move mv[],int num)//保存移动数组
	{
		int p;
		p=board[to];
		piece[0][board[from]]=to;
		if(p!=0)
			piece[0][p]=0;
		board[to]=board[from];
		board[from]=0;
		
		int r=Check(side);
		board[from]=board[to];
		board[to]=p;
		piece[0][board[from]]=from;
		if(p!=0)
			piece[0][p]=to;
			if(r==0)
			{
				mv[num]=new Move();
				mv[num].from=from;
				mv[num].to=to;
			}
	}
	 
	public int Check(int nowSide)//检测nowSide一方是否被将军
	{
		//wKing bKing 分别代表红,黑双方帅(将)的位置
		int wKing=piece[0][16];
		int bKing=piece[0][32];
		//定义一个敌方棋子的基础大小,比如红方的基础大小是16,黑方的基础大小是32
		int LSideTag=(1-nowSide)*16+16;
		//q为要检测是否被将军 的帅或将的位置
		int q=piece[0][nowSide*16+16];
		//是否被将军标志 为1是被将军
		int r=0;
		//m表示第几个当前棋子 比如5个兵 2个马 之类的
		int m;
		//k表示是第几个方位  比如马走八方,车动四方
		int k;
		//n表示目标的下一个位置
		int n;
		//nf表示目标位置的下一个坎,比如马有失蹄,象有被堵
		int nf;
		//overFlag表示炮的翻山标志,为1是已经翻山,为0时未翻山
		int overFlag;
		
		int j,p;
		//检测将帅是否相见
		if((wKing%16)==(bKing%16))
		{
			r=1;
			for(j=bKing+16;j<wKing;j=j+16)
			{
				if(board[j]!=0)
				{
					r=0;
					break;
				}
			}
			if(r==1)
				return 1;
		}

		
		//检测马是否将军
		for(m=5;m<=6;m++)
		{
			p=piece[0][LSideTag+m];
			if(p==0)
				continue;
			for(k=0;k<8;k++)
			{
				n=p+horse[k];
				if(n==q)
				{
					nf=p+horseFeet[k];
					if(board[nf]==0)
					{
						r=1;
						return r;
					}
				}
			}
		}
		
		//检测车是否将军
		for(m=7;m<=8;m++)
		{
			p=piece[0][LSideTag+m];
			if(p==0)
				continue;
			for(k=0;k<4;k++)
			{
				for(j=1;j<10;j++)
				{
					n=p+j*king[k];
					try{
						if(n<256&&n>0)
						{
							if(board[n]==0)
							{
								continue;
							}
							else
							{
								if(n==q)
								{
									r=1;
									return r;
								}
								break;
							}
						}
					}
					catch(Exception e){}
				}
			}
		}
		//检测炮是否将军
		for(m=9;m<=10;m++)
		{
			overFlag=0;
			p=piece[0][LSideTag+m];
			if(p==0)
				continue;
			//如果炮 将同列
			if(p%16==q%16)
			{
				int posAdd=(p>q?-16:16);
				for(p=p+posAdd;p!=q;p=p+posAdd)
				{
					if(board[p]!=0)
					{
						if(overFlag==0)
						{
							overFlag=1;
						}
						else
						{
							overFlag=2;
							break;
						}
					}
				}
				if(overFlag==1)
					return 1;
			}
			else if(p/16==q/16)
			{
				int posAdd=(p>q?-1:1);
				for(p=p+posAdd;p!=q;p=p+posAdd)
				{
					if(board[p]!=0)
					{
						if(overFlag==0)
						{
							overFlag=1;
						}
						else
						{
							overFlag=2;
							break;
						}
					}
				}
				if(overFlag==1)
					return 1;
			}
		}
		
		//检测兵是否将军
		for(m=11;m<=15;m++)
		{
			p=piece[0][LSideTag+m];
			if(p==0)
				continue;
			for(k=0;k<3;k++)
			{
				n=p+soldier[1-nowSide][k];
				if(n==q)
				{
					return 1;
				}
			}
		}
		return 0;
	}
	
	//将的走法生成,p为将的当前位置
	
	public int genAllMove(Move mv[])//所有棋子的移动
	{
		kingMove(mv);
		horseMove(mv);
		elephentMove(mv);
		knightMove(mv);
		cannonMove(mv);
		vehicleMove(mv);
		soldierMove(mv);
		
		return moveNum;
	}
	public void kingMove(Move mv[])//将帅移动
	{
		//k为能走的方位,n为下一步的地方
		int k,n,p;
		int SideTag=16+side*16;
		p=piece[0][SideTag];
		for(k=0;k<4;k++)
		{
			
			n=p+king[k];
			if(n>=0&&n<256)
			{
				if((border[n]&1)!=0)
				{
					if((board[n]&SideTag)==0)
					{
						saveMove(p,n,mv);
						moveArrayNum++;
					}
					else
						continue;
				}
				else
					continue;
			}
		}
	}
	
	//马的走法生成,p为将的当前位置
	public void horseMove(Move mv[])//马的移动
	{
		int nf;
		int k,n,m,p;
		//m为第几个棋子 比如说有两个马 
		int SideTag=16+side*16;
		for(m=5;m<=6;m++)
		{
			p=piece[0][SideTag+m];
			if(p==0)
			{
				continue;
			}
			for(k=0;k<8;k++)
			{
				n=p+horse[k];
				nf=p+horseFeet[k];
				if(n>=0&&n<256)
				{
					if((border[n]&8)!=0)
					{
						if((board[n]&SideTag)==0)
						{
							if(board[nf]==0)
							{
								saveMove(p,n,mv);
								moveArrayNum++;
							}
						}
						else
							continue;
					}
					else
						continue;
				}
			}
		}
	}
	
	//象的走法生成
	public void elephentMove(Move mv[])//象的移动
	{
		int nf;
		int k,n,m,p;
		//m为第几个棋子 比如说有两个马 
		int SideTag=16+side*16;
		for(m=3;m<=4;m++)
		{
			p=piece[0][SideTag+m];
			if(p==0)
			{
				continue;
			}
			for(k=0;k<4;k++)
			{
				n=p+elephant[k];
				nf=p+elephantFeet[k];
				if((border[n]&4)!=0)
				{
					if((board[n]&SideTag)==0)
					{
						if(board[nf]==0)
						{
							saveMove(p,n,mv);
							moveArrayNum++;
						}
					}
					else
						continue;
				}
				else
					continue;
			}
		}
	}
	
	//士的走法生成
	public void knightMove(Move mv[])//士的移动
	{
		int n,k,p,m;
		int SideTag=16+side*16;
		for(m=1;m<=2;m++)
		{
			p=piece[0][SideTag+m];
			if(p==0)
				continue;
			for(k=0;k<4;k++)
			{
				n=p+knight[k];
				if((border[n]&2)!=0)
				{
					if((board[n]&SideTag)==0)
					{
						saveMove(p,n,mv);
						moveArrayNum++;
					}
				}
			}
		}
	}
	
	//炮的走法生成
	public void cannonMove(Move mv[])//炮的移动
	{
		int n,k,p,m;
		int j;
		int overFlag=0;
		int SideTag=16+side*16;
		for(m=9;m<=10;m++)
		{
			p=piece[0][SideTag+m];
			if(p==0)
				continue;
			for(k=0;k<4;k++)
			{
				overFlag=0;
				for(j=1;j<10;j++)
				{
					n=p+j*king[k];
					if((border[n]&8)!=0)
					{
						if(board[n]==0)
						{
							if(overFlag==0)
							{
								saveMove(p,n,mv);
								moveArrayNum++;
							}
						}
						else
						{
							if(overFlag==0)
							{
								overFlag=1;
							}
							else
							{
								if((board[n]&SideTag)==0)
								{
									saveMove(p,n,mv);
									moveArrayNum++;
								}
								break;
							}
							continue;
						}
					}
					else
					{
						break;
					}
				}
			}
		}
	}
	
	//车的走法生成
	public void vehicleMove(Move mv[])//车的移动
	{
		int n,k,p,m;
		int j;
		int SideTag=16+side*16;
		for(m=7;m<=8;m++)
		{
			p=piece[0][SideTag+m];
			if(p==0)
				continue;
			for(k=0;k<4;k++)
			{
				for(j=1;j<10;j++)
				{
					n=p+j*king[k];
					if((border[n]&8)!=0)
					{
						if(board[n]==0)
						{
							saveMove(p,n,mv);
							moveArrayNum++;
						}
						else if(board[n]!=0)
						{
							if((board[n]&SideTag)==0)
							{
								saveMove(p,n,mv);
								moveArrayNum++;
								break;
							}
							else
							{
								break;
							}
						}
					}
					else 
						break;
				}
			}
		}
	}
	
	//兵的走法生成
	public void soldierMove(Move mv[])//兵的移动
	{
		int n,k,p,m;
		int SideTag=16+side*16;
		for(m=11;m<=15;m++)
		{
			p=piece[0][SideTag+m];
			if(p==0)
				continue;
			for(k=0;k<3;k++)
			{
				n=p+soldier[side][k];
				if((border[n]&SideTag)!=0)
				{
					if((board[n]&SideTag)==0)
					{
						saveMove(p,n,mv);
						moveArrayNum++;
					}
				}
			}
		}
	}
	
	public boolean makeMove(Move m)//开始移动
	{
		int from,dest,p;
		int pt,pc;
		int SideTag=(side==0?32:16);
		
		from=m.from;
		dest=m.to;
		
		moveStack[stackTop]=new Move();
		moveStack[stackTop].from=from;
		
		moveStack[stackTop].to=dest;
		moveStack[stackTop].capture=p=board[dest];
		stackTop++;
		
		//设置棋子数组
		if(p>0)
		{
			piece[0][p] = 0;
			pt = PieceNumToType[p];
			if (p>=32)
			{
				pt += 7;
			}
			ZobristKey ^= ZobristTable[pt][dest];
			ZobristKeyCheck ^= ZobristTableCheck[pt][dest];
		}
		piece[0][board[from]]=dest;
		
		//设置棋盘数组
		pc=board[dest]=board[from];
		board[from]=0;
		pt = PieceNumToType[pc];
		if (pc>=32) 
		{
			pt += 7;
		}
		ZobristKey ^= ZobristTable[pt][dest] ^ ZobristTable[pt][from];
		ZobristKeyCheck ^= ZobristTableCheck[pt][dest] ^ ZobristTableCheck[pt][from];
		ply++;
		changeSide();
		
		if(p==SideTag)
			return true;
		return false;
	}
	public void unMakeMove()//退出刚刚的移动
	{
		int from,dest,p;
		int pt,pc;
		stackTop--;
		ply--;
		side=1-side;
		
		from=moveStack[stackTop].from;
		dest=moveStack[stackTop].to;
		p=moveStack[stackTop].capture;
		
		//设置棋盘数组
		pc=board[from]=board[dest];
		board[dest]=p;
		pt = PieceNumToType[pc];
		if (pc>=32) 
		{
			pt += 7;
		}
		ZobristKey ^= ZobristTable[pt][from] ^ ZobristTable[pt][dest];
		ZobristKeyCheck ^= ZobristTableCheck[pt][from] ^ ZobristTableCheck[pt][dest];
		
		//设置棋子数组
		if(p>0)
		{
			piece[0][p] = dest;
			pt = PieceNumToType[p];
			if (p>=32)
			{
				pt += 7;
			}
			ZobristKey ^= ZobristTable[pt][dest];
			ZobristKeyCheck ^= ZobristTableCheck[pt][dest];

		}
		piece[0][board[from]]=from;
	}
	
	public int AlphaBetaSearch(int depth,int alpha,int beta)//alphaBeta算法的实现
	{
		int value;
		int best;
		int numArray=0;
		Move goodMove=new Move();
		Move moveArray[]=new Move[512];
		for(int i=0;i<512;i++)
		{
			moveArray[i]=new Move();
		}
		Move ma=new Move();//存储临时最佳走法
		Move NULL_MOVE=new Move();
		int num;
		int i;
		//ma=bestMove=NULL_MOVE;
		value=ReadHashTable(depth,alpha,beta,ma);
		if(value!=NOVALUE)
		{
			return value;
		}
		best=-NOVALUE;
		int alphaFlag=1;
			
		moveNum=0;
		num=0;
		moveArrayNum=0;
//		num=genAllMove(moveArray);
//		System.out.println("moveArrayNum="+moveArrayNum);
//		System.out.println("moveNum="+moveNum);
		if(ma.from!=0)
		{
			moveArray[0]=ma;
			num=GenCapMove(moveArray,1);
			num=num+GenNonCapMove(moveArray,1+num);
			num=num+1;
		}
		else
		{
			num=GenCapMove(moveArray,0);
			num=num+GenNonCapMove(moveArray,num);
		}
		if(depth==0)
		{
			value=Eval();
			SaveHashTable(value,depth,hashEXACT,NULL_MOVE);
			return value;
		}
		for(i=0;i<num;i++)
		{
//			System.out.println("form="+moveArray[i].from+"  to="+moveArray[i].to);
			ma=moveArray[i];
			if(makeMove(ma))
			{
				value=depth;
			}
			else
			{
				value=-AlphaBetaSearch(depth-1,-beta,-alpha);
			}
			unMakeMove();
//			System.out.println("value="+value+"  beta="+beta+"  alpha="+alpha);
			if(value>=beta)
			{
				SaveHashTable(beta,depth,hashBETA,moveArray[i]);
				return value;
			}
			if(value>best)
			{
//				System.out.println("value="+value+"alpha="+alpha);
				best=value;
				goodMove=moveArray[i];
				if(value>alpha)
				{
					alphaFlag=0;
					alpha=value;
//					System.out.println("finally value="+value+"finally alpha="+alpha);
					if(depth==MaxDepth)
					{
						bestMove=ma;
						bestMove.capture=board[bestMove.from];
					}
				}
			}
			if(alphaFlag==1)
			{
				SaveHashTable(alpha,depth,hashALPHA,goodMove);
			}
			else
			{
				SaveHashTable(best,depth,hashEXACT,goodMove);
			}
		}
		return best;
	}
	public int ReadHashTable(int depth,int alpha,int beta,Move mv)
	{
		long add= ZobristKey & HashMask;
		
		int temp=0;
		HashNode A=new HashNode();;
		A = hashTable[(int) add];
		if (A.check == ZobristKeyCheck) 
		{
			if (A.value > WinValue)
			{
				temp = A.value - ply;
			}
			if (A.value < -WinValue) 
			{
				temp = A.value + ply;
			}
			if (A.depth > depth) 
			{
				if (A.flag == hashEXACT) 
				{
					return temp;
				}
				if (A.flag == hashALPHA && temp <= alpha)
				{
					return alpha;
				}
				if (A.flag == hashBETA && temp >= beta)
				{
					return beta;
				}
				mv = A.goodMove;
			}
		}
		return NOVALUE;
	}
	
	public void SaveHashTable(int value,int depth,int type,Move mv)
	{
		{
			long add = ZobristKey & HashMask;
			
			HashNode A=new HashNode();
			if (value > WinValue)
			{
				value = value + ply;
			}
			if (value < -WinValue) 
			{
				value = value - ply;
			}
			A.value = value;
			A.check = ZobristKeyCheck;
			A.depth = depth;
			A.flag  = type;
			A.goodMove = mv;
			if(hashTable[(int) add].check == ZobristKeyCheck)
			{
				if(hashTable[(int) add].depth < depth)
					hashTable[(int) add] = A;
			}
			else
				hashTable[(int) add] = A;
		}
	}
	
	public int Eval()//局面价值判断
	{
		int value;
		int redValue=0;
		int blackValue=0;
		int j;
		
		for(j=16;j<32;j++)
		{
			if(piece[0][j]!=0)
			{
				redValue=redValue+piece[1][j]+PositionValue[0][PieceNumToType[j]][piece[0][j]];
			}
		}
		for(j=32;j<48;j++)
		{
			if(piece[0][j]!=0)
			{
				blackValue=blackValue+piece[1][j]+PositionValue[0][PieceNumToType[j]][piece[0][j]];
			}
		}
		
		if(side==0)
		{
			value=redValue-blackValue;
			return value;
		}
		else
		{
			value=blackValue-redValue;
			return value;
		}
	}
	public void computerThink()
	{
		bestMove.from=0;
		bestMove.to=0;
		ply=0;
		stackTop=0;
		MaxDepth=4;
		AlphaBetaSearch(MaxDepth,maxAlpha,maxBeta);
	}
	public int legalMove(Move mv)
	{
		Move arrayMove[]=new Move[512];
		int num;
		int i;
		moveNum=0;
		num=genAllMove(arrayMove);
//		System.out.println("num="+num);
		for(i=0;i<num;i++)
		{
			//System.out.println("from="+arrayMove[i].from+"to="+arrayMove[i].to);
			if(mv.from==arrayMove[i].from&&mv.to==arrayMove[i].to)
			{
				return 1;
			}
		}
		
		return 0;
	}
	public void IniZobrist()
	{
		int i,j;
		Random rand=new Random();
		long RandSeed=1;
		rand.setSeed(RandSeed);
		ZobristPlayer=rand.nextLong();
		for(i=0;i<14;i++)
		{
			for(j=0;j<256;j++)
			{
				ZobristTable[i][j]=rand.nextLong();
			}
		}
		ZobristPlayerCheck=rand.nextLong();
		for(i=0;i<14;i++)
		{
			for(j=0;j<256;j++)
			{
				ZobristTableCheck[i][j]=rand.nextLong();
			}
		}
	}
	
	public int GenNonCapMove(Move[] moveArray,int num)
	{
		int i,j,k;
		int p;	//p:棋子位置
		int n;	//下一步可能行走的位置
		int m;	//马腿、象眼位置
		int SideTag;		//走棋方，经方16，黑方32
		int OverFlag;		//炮的翻山标志
		int mvArray=num;

		SideTag = 16 + 16 * side;
		
		p = piece[0][SideTag];	//将的位置
		if(p==0)
			return 0;

		//将的走法
		for(k=0; k<4; k++)//4个方向
		{
			n = p + king[k];	//n为新的可能走到的位置
			if((LegalPosition[side][n] & PositionMask[0])!=0)	//将对应下标为0
			{
				if( board[n]==0 )	//目标位置上没有棋子
				{
					saveMove(p, n, moveArray,mvArray);
					mvArray++;
				}
			}
		}

		//士的走法
		for(i=1; i<=2; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<4; k++)//4个方向
			{
				n = p + knight[k];	//n为新的可能走到的位置
				if((LegalPosition[side][n] & PositionMask[1])!=0)	//士对应下标为1
				{
					if(  board[n]==0 )	//目标位置上没有棋子
					{
						saveMove(p, n, moveArray,mvArray);
						mvArray++;
					}
				}
			}
		}

		//象的走法
		for(i=3; i<=4; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<4; k++)//4个方向
			{
				n = p + elephant[k];	//n为新的可能走到的位置
				if((LegalPosition[side][n] & PositionMask[2])!=0)	//象对应下标为2
				{
					m = p + elephantFeet[k];
					if(board[m]==0)	//象眼位置无棋子占据
					{
						if(  board[n]==0 )	//目标位置上没有棋子
						{
							saveMove(p, n, moveArray,mvArray);
							mvArray++;
						}
					}
				}
			}
		}
		
		//马的走法
		for(i=5; i<=6; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<8; k++)//8个方向
			{
				n = p + horse[k];	//n为新的可能走到的位置
				if((LegalPosition[side][n] & PositionMask[3])!=0)	//马对应下标为3
				{
					m = p + horseFeet[k];
					if(board[m]==0)	//马腿位置无棋子占据
					{
						if(  board[n]==0 )	//目标位置上没有棋子
						{
							saveMove(p, n, moveArray,mvArray);
							mvArray++;
						}
					}
				}
			}
		}

		//车的走法
		for(i=7; i<=8; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<4; k++)	//4个方向
			{
				for(j=1; j<10; j++)	//横的最多有8个可能走的位置，纵向最多有9个位置
				{
					n = p + j * king[k];
					if((LegalPosition[side][n] & PositionMask[4])==0)	//车对应下标为4
						break;//不合理的位置
					if( board[n] ==0)	//目标位置上无子
					{
						saveMove(p, n, moveArray,mvArray);
						mvArray++;
					}
					else 	//目标位置上有棋子,退出内层循环
						break;
				}
			}
		}

		//炮的走法
		for(i=9; i<=10; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<4; k++)	//4个方向
			{
				OverFlag = 0;
				for(j=1; j<10; j++)	//横的最多有8个可能走的位置，纵向最多有9个位置
				{
					n = p + j * king[k];
					if((LegalPosition[side][n] & PositionMask[5])==0)	//炮对应下标为5
						break;//不合理的位置
					if( board[n]==0 )	//目标位置上无子
					{
						if(OverFlag==0)	//未翻山
						{
							saveMove(p, n, moveArray,mvArray);
							mvArray++;
						}
						//已翻山则不作处理，自动考察向下一个位置
					}
					else//目标位置上有子
					{
							break;	//不论吃不吃子，都退出此方向搜索
					}
				}
			}	
		}

		//兵的走法
		for(i=11; i<=15; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<3; k++)//3个方向
			{
				n = p + soldier[side][k];	//n为新的可能走到的位置
				if((LegalPosition[side][n] & PositionMask[6])!=0)	//兵对应下标为6
				{
					if( board[n]==0 )	//目标位置上没有本方棋子
					{
						saveMove(p, n, moveArray,mvArray);
						mvArray++;
					}
				}
			}
		}
		return mvArray-num;
	}
	public int GenCapMove(Move[] MoveArray,int num)
	{
		short i,j,k;
		int p;	//p:棋子位置
		int n;	//下一步可能行走的位置
		int m;	//马腿、象眼位置
		int OpChess;	//被吃的对方棋子
		int value; //价值
		int SideTag;		//走棋方，红方16，黑方32
		int OpSideTag;		//对方标志
		int OverFlag;		//炮的翻山标志
		int mvArray=num;
		//move * mvArray = MoveArray;

		SideTag = 16 + 16 * side;
		OpSideTag = 48 - SideTag;
		
		p = piece[0][SideTag];	//将的位置
		if(p!=0)
			return 0;

		//将的走法
		for(k=0; k<4; k++)//4个方向
		{
			n = p + king[k];	//n为新的可能走到的位置
			if((LegalPosition[side][n] & PositionMask[0])!=0)	//将对应下标为0
			{
				OpChess = board[n];
				if( (OpChess & OpSideTag)!=0)	//目标位置上有对方棋子
				{
					saveMove(p, n, MoveArray,mvArray);
					value = MvvValues[OpChess] - ((Protected(1 - side, n)!=0) ? 8 : 0) ;// 帅(将)的价值是8
					if(value>0)
					{
						MoveArray[mvArray].capture=value;
					}
					else
					{
						MoveArray[mvArray].capture=0;
					}
					mvArray++;
				}
			}
		}

		//士的走法
		for(i=1; i<=2; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<4; k++)//4个方向
			{
				n = p + knight[k];	//n为新的可能走到的位置
				if((LegalPosition[side][n] & PositionMask[1])!=0)	//士对应下标为1
				{
					OpChess = board[n];
					if( (OpChess & OpSideTag)!=0)	//目标位置上有对方棋子
					{
						saveMove(p, n, MoveArray,mvArray);
						value = MvvValues[OpChess] - ((Protected(1 - side, n)!=0) ? 2 : 0) ;// 士的价值是2
						if(value>0)
						{
							MoveArray[mvArray].capture=value;
						}
						else
						{
							MoveArray[mvArray].capture=0;
						}
						mvArray++;
					}
				}
			}
		}

		//象的走法
		for(i=3; i<=4; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<4; k++)//4个方向
			{
				n = p + elephant[k];	//n为新的可能走到的位置
				if((LegalPosition[side][n] & PositionMask[2])!=0)	//象对应下标为2
				{
					m = p + elephantFeet[k];
					if(board[m]==0)	//象眼位置无棋子占据
					{
						OpChess = board[n];
						if((OpChess & OpSideTag)!=0)	//目标位置上有对方棋子
						{
							saveMove(p, n, MoveArray,mvArray);
							value = MvvValues[OpChess] - ((Protected(1 - side, n)!=0) ? 2 : 0) ;// 象的价值是2
							if(value>0)
							{
								MoveArray[mvArray].capture=value;
							}
							else
							{
								MoveArray[mvArray].capture=0;
							}
							mvArray++;
						}
					}
				}
			}
		}
		
		//马的走法
		for(i=5; i<=6; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<8; k++)//8个方向
			{
				n = p + horse[k];	//n为新的可能走到的位置
				if((LegalPosition[side][n] & PositionMask[3])!=0)	//马对应下标为3
				{
					m = p + horseFeet[k];
					if(board[m]==0)	//马腿位置无棋子占据
					{
						OpChess = board[n];
						if( (OpChess & OpSideTag)!=0)	//目标位置上有对方棋子
						{
							saveMove(p, n, MoveArray,mvArray);
							value = MvvValues[OpChess] - ((Protected(1 - side, n)!=0) ? 2 : 0) ;// 马的价值是4
							if(value>0)
							{
								MoveArray[mvArray].capture=value;
							}
							else
							{
								MoveArray[mvArray].capture=0;
							}
							mvArray++;
						}
					}
				}
			}
		}

		//车的走法
		for(i=7; i<=8; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<4; k++)	//4个方向
			{
				for(j=1; j<10; j++)	//横的最多有8个可能走的位置，纵向最多有9个位置
				{
					n = p + j * king[k];
					if((LegalPosition[side][n] & PositionMask[4])==0)	//车对应下标为4
						break;//不合理的位置
					OpChess = board[n];
					if(OpChess==0 )	//目标位置上无子，不作处理
					{
					}
					else if ( (OpChess & SideTag)!=0)	//目标位置上有本方棋子
						break;
					else	//目标位置上有对方棋子
					{
						saveMove(p, n, MoveArray,mvArray);
						value = MvvValues[OpChess] - ((Protected(1 - side, n)!=0) ? 2 : 0) ;// 车的价值是6
						if(value>0)
						{
							MoveArray[mvArray].capture=value;
						}
						else
						{
							MoveArray[mvArray].capture=0;
						}
						mvArray++;
						break;
					}
				}
			}
		}

		//炮的走法
		for(i=9; i<=10; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<4; k++)	//4个方向
			{
				OverFlag = 0;
				for(j=1; j<10; j++)	//横的最多有8个可能走的位置，纵向最多有9个位置
				{
					n = p + j * king[k];
					if((LegalPosition[side][n] & PositionMask[5])==0)	//炮对应下标为5
						break;//不合理的位置
					OpChess = board[n];
					if(OpChess==0 )	//目标位置上无子
					{
						//不作处理，自动考察向下一个位置
					}
					else//目标位置上有子
					{
						if (OverFlag==0)	//未翻山则置翻山标志
							OverFlag = 1;
						else	//已翻山
						{
							if((OpChess & OpSideTag)!=0)//对方棋子
							{
								saveMove(p, n, MoveArray,mvArray);
								value = MvvValues[OpChess] - ((Protected(1 - side, n)!=0) ? 2 : 0) ;// 炮的价值是4
								if(value>0)
								{
									MoveArray[mvArray].capture=value;
								}
								else
								{
									MoveArray[mvArray].capture=0;
								}
								mvArray++;
							}
							break;	//不论吃不吃子，都退出此方向搜索
						}
					}
				}
			}	
		}

		//兵的走法
		for(i=11; i<=15; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<3; k++)//3个方向
			{
				n = p + soldier[side][k];	//n为新的可能走到的位置
				if((LegalPosition[side][n] & PositionMask[6])!=0)	//兵对应下标为6
				{
					OpChess = board[n];
					if( (OpChess & OpSideTag)!=0)	//目标位置上有对方棋子
					{
						saveMove(p, n, MoveArray,mvArray);
						value = MvvValues[OpChess] - ((Protected(1 - side, n)!=0) ? 2 : 0) ;// 兵的价值是2
						if(value>0)
						{
							MoveArray[mvArray].capture=value;
						}
						else
						{
							MoveArray[mvArray].capture=0;
						}
						mvArray++;
					}
				}
			}
		}
		return mvArray-num;
	}
	public int Protected(int lSide,int dst)
	{
		int p;
		int r;	
		int SideTag = 16 + lSide * 16;	//此处表示lSide将的值
		int i,k;	//循环变量
		int PosAdd;	//位置增量
		int n;//下一步可能行走的位置
		int m;//马腿位置

		if (lSide == 0 ? (dst & 0x80) != 0 : (dst & 0x80) == 0) //判断棋子是否过河,未过河受帅,仕,象保护
		{
			//检测是否被将保护
			p = piece[0][SideTag];
			if(p!=0&& p!=dst)
			{
				for(k=0; k<4; k++)//4个方向
				{
					n = p + horse[k];	//n为新的可能走到的位置
					if(n!=dst)
						continue;

					if((LegalPosition[lSide][n] & PositionMask[0])!=0)	//将对应下标为0
					{
						return 1;
					}
				}
			}

			//检测是否被士保护
			for(i=1;i<=2;i++)
			{
				p = piece[0][SideTag+i];
				if(p==0)
					continue;
				if(p==dst)
					continue;
				for(k=0; k<4; k++)//4个方向
				{
					n = p + horse[k];	//n为新的可能走到的位置
					if(n!=dst)
						continue;

					if((LegalPosition[lSide][n] & PositionMask[1])!=0)	//士对应下标为1
					{
						return 1;
					}
				}	
			}
			//检测是否被相保护
			for(i=3;i<=4;i++)
			{
				p = piece[0][SideTag+i];
				if(p==0)
					continue;
				if(p==dst)
					continue;
				for(k=0; k<4; k++)//4个方向
				{
					n = p + horse[k];	//n为新的可能走到的位置
					if(n!=dst)
						continue;

					if((LegalPosition[lSide][n] & PositionMask[2])!=0)	//相对应下标为2
					{
						m = p + elephantFeet[k];
						if(board[m]==0)	//相眼位置无棋子占据
						{
							return 1;
						}
					}
				}	
			}
		}


		//检测是否被马保护
		for(i=5;i<=6;i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			if(p==dst)
				continue;
			for(k=0; k<8; k++)//8个方向
			{
				n = p + horse[k];	//n为新的可能走到的位置
				if(n!=dst)
					continue;

				if((LegalPosition[lSide][n] & PositionMask[3])==0)	//马将对应下标为3
				{
					m = p + horseFeet[k];
					if(board[m]==0)	//马腿位置无棋子占据
					{
						return 1;
					}
				}
			}
		}
		
		//检测将是否被车攻击
		r=1;
		for(i=7;i<=8;i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			if(p==dst)
				continue;
			if(p%16 == dst%16)	//在同一纵线上
			{
				PosAdd = (p>dst?-16:16);
				for(p=p+PosAdd; p!=dst; p = p+PosAdd)
				{
					if((board[p])!=0)	//车将中间有子隔着
					{
						r=0;
						break;
					}
				}
				if(r!=0)
					return r;
			}
			else if(p/16 ==dst/16)	//在同一横线上
			{
				PosAdd = (p>dst?-1:1);
				for(p=p+PosAdd; p!=dst; p = p+PosAdd)
				{
					if(board[p]!=0)
					{
						r=0;
						break;
					}
				}
				if(r!=0)
					return r;
			}
		}
		
		//检测将是否被炮攻击
		int OverFlag = 0;	//翻山标志
		for(i=9;i<=10;i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			if(p==dst)
				continue;
			if(p%16 == dst%16)	//在同一纵线上
			{
				PosAdd = (p>dst?-16:16);
				for(p=p+PosAdd; p!=dst; p = p+PosAdd)
				{
					if(board[p]!=0)
					{
						if(OverFlag==0)	//隔一子
							OverFlag = 1;
						else			//隔两子
						{
							OverFlag = 2;
							break;
						}
					}
				}
				if(OverFlag==1)
					return 1;
			}
			else if(p/16 ==dst/16)	//在同一横线上
			{
				PosAdd = (p>dst?-1:1);
				for(p=p+PosAdd; p!=dst; p = p+PosAdd)
				{
					if(board[p]!=0)
					{
						if(OverFlag==0)
							OverFlag = 1;
						else
						{
							OverFlag = 2;
							break;
						}
					}
				}
				if(OverFlag==1)
					return 1;
			}
		}

		//检测将是否被兵攻击
		for(i=11;i<=15;i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			if(p==dst)
				continue;
			for(k=0; k<3; k++)//3个方向
			{
				n = p + soldier[lSide][k];	//n为新的可能走到的位置
				if((n==dst) && (LegalPosition[lSide][n] & PositionMask[6])!=0)	//兵士将对应下标为6
				{
					return 1;
				}
			}
		}

		return 0;
	}
	public void NewHashTable()
	{
		HashMask=1024*1024-1;
		for(int i=0;i<1024*1024;i++)
		{
			hashTable[i]=new HashNode();
		}
		IniZobrist();
	}
}

class Move
{
	int from, to;
	int capture;
	public Move()
	{
		from=0;
		to=0;
		capture=0;
	}
}

class HashNode
{
	long check;
	int depth;
	int value;
	int flag;
	Move goodMove=new Move();
}












