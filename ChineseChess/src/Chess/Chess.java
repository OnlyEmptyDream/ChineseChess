package Chess;

import java.util.Random;

public class Chess 
{
	static final int hashEXACT=0;
	static final int hashALPHA=1;
	static final int hashBETA=2;
	Move g_KillerMove[][]=new Move[256][2];       // ɱ���ŷ���
	char g_History[]=new char[65536];  // ��ʷ��
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
	long ZobristPlayer;//32λ���巽��ֵ
	long ZobristPlayerCheck;//64λ���巽У��ֵ
	long ZobristTable[][]=new long[14][256];//32λ�����������̸�λ�õļ�ֵ
	long ZobristTableCheck[][]=new long[14][256];//64λ���������̸�λ�õ�У��ֵ
	long ZobristKey;
	long ZobristKeyCheck;
	long HashMask;
	//moveArray����Ŀ
	int moveArrayNum;
	//side ��ǰ���巽 �����0,��ô�Ǻ췽����,�����1,��ô�Ǻڷ�����
	int side;
	//moevNum Ϊ����ĺ����߷�������
	int moveNum;
	//�߷�ջ
	Move moveStack[]=new Move[128];
	//ջ��ָ��
	int stackTop;
	//��ǰ������߷�
	static Move bestMove=new Move();
	//��ǰ�������
	int ply;
	//����������
	int MaxDepth;
	//��Сvalueֵ
	int maxAlpha;
	//���betaֵ
	int maxBeta;
	// MVV��ֵ���ǰ���˧(��)=8����=6������=4������=2�趨��
	int MvvValues[] = {
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  8, 2, 2, 2, 2, 4, 4, 6, 6, 4, 4, 2, 2, 2, 2, 2,
	  8, 2, 2, 2, 2, 4, 4, 6, 6, 4, 4, 2, 2, 2, 2, 2,
	};
	//�߽�����
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
	
	//����λ�÷�ֵ
	int []PieceNumToType=
		{
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,1,1,2,2,3,3,4,4,5,5,6,6,6,6,6,
				0,1,1,2,2,3,3,4,4,5,5,6,6,6,6,6,
		};
	
	int [][][]PositionValue=
		{
				//�ڷ�
				{
					{//�� ˧
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,0,0,0,15,20,15,0,0,0,0,0,0,0,
						0,0,0,0,0,0,10,10,10,0,0,0,0,0,0,0,
						0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						//���Ӻ���
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//��ʿ
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,0,0,0,30,0,30,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,22,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,30,0,30,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						//���Ӻ���
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//����
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,	0,0,30,0,0,0,30,0,0,		0,0,0,0,
						0,0,0,	0,0,0,0,0,0,0,0,0,			0,0,0,0,
						0,0,0,	20,0,0,0,35,0,0,0,20,		0,0,0,0,
						0,0,0,	0,0,0,0,0,0,0,0,0,			0,0,0,0,
						0,0,0,	0,0,25,0,0,0,25,0,0,		0,0,0,0,

						//���Ӻ���
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//����
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,		60,70,75,70,60,70,75,70,60,				0,0,0,0,
						0,0,0,		70,75,75,70,50,70,75,75,70,				0,0,0,0,
						0,0,0,		80,80,90,90,80,90,90,80,80,				0,0,0,0,
						0,0,0,		80,90,100,100,90,100,100,90,80,			0,0,0,0,
						0,0,0,		90,100,100,110,100,110,100,100,90,		0,0,0,0,
						//���Ӻ���
						0,0,0,		90,110,110,120,100,120,110,110,90,		0,0,0,0,
						0,0,0,		90,100,120,130,110,130,120,100,90,		0,0,0,0,
						0,0,0,		90,100,120,125,120,125,120,100,90,		0,0,0,0,
						0,0,0,		80,110,125,90,70,90,125,110,80,			0,0,0,0,
						0,0,0,		70,80,90,80,70,80,90,80,70,				0,0,0,0,

						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//�쳵
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,		150,160,150,160,150,160,150,160,150,	0,0,0,0,
						0,0,0,		160,170,160,160,150,160,160,170,160,	0,0,0,0,
						0,0,0,		170,180,170,170,160,170,170,180,170,	0,0,0,0,
						0,0,0,		170,190,180,220,240,220,200,190,170,	0,0,0,0,
						0,0,0,		180,220,210,240,250,240,210,220,180,	0,0,0,0,
						//���Ӻ���
						0,0,0,		180,220,210,240,250,240,210,220,180,	0,0,0,0,
						0,0,0,		180,220,210,240,250,240,210,220,180,	0,0,0,0,
						0,0,0,		170,190,200,220,240,220,200,190,170,	0,0,0,0,
						0,0,0,		170,180,170,190,250,190,170,180,170,	0,0,0,0,
						0,0,0,		160,170,160,150,150,150,160,170,160,	0,0,0,0,

						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//����
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,		80,90,80,70,60,70,80,90,80,			0,0,0,0,
						0,0,0,		80,90,80,70,65,70,80,90,80,			0,0,0,0,
						0,0,0,		90,100,80,80,70,80,80,100,90,		0,0,0,0,
						0,0,0,		90,100,90,90,110,90,90,100,90,		0,0,0,0,
						0,0,0,		90,100,90,110,130,110,90,100,90,	0,0,0,0,
						//���Ӻ���
						0,0,0,		90,110,90,110,130,110,90,110,90,	0,0,0,0,
						0,0,0,		90,110,90,110,130,110,90,110,90,	0,0,0,0,
						0,0,0,		100,120,90,80,80,80,90,120,100,		0,0,0,0,
						0,0,0,		110,125,100,70,60,70,100,125,110,	0,0,0,0,
						0,0,0,		125,130,100,70,60,70,100,130,125,	0,0,0,0,

						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//���
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,		10,0,15,0,15,0,15,0,10,			0,0,0,0,
						0,0,0,		10,0,15,0,15,0,15,0,10,			0,0,0,0,
						//���Ӻ���
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
				//�ڷ�
				{
					{//��˧
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						//���Ӻ���
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,
						0,0,0,0,0,0,10,10,10,0,0,0,0,0,0,0,
						0,0,0,0,0,0,15,20,15,0,0,0,0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//��ʿ
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						//���Ӻ���
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,30,0,30,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,22,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,30,0,30,0,0,0,0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//����
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						//���Ӻ���
						0,0,0,	0,0,25,0,0,0,25,0,0,		0,0,0,0,
						0,0,0,	0,0,0,0,0,0,0,0,0,			0,0,0,0,
						0,0,0,	20,0,0,0,35,0,0,0,20,		0,0,0,0,
						0,0,0,	0,0,0,0,0,0,0,0,0,			0,0,0,0,
						0,0,0,	0,0,30,0,0,0,30,0,0,		0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//����
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,		70,80,90,80,70,80,90,80,70,				0,0,0,0,
						0,0,0,		80,110,125,90,70,90,125,110,80,			0,0,0,0,
						0,0,0,		90,100,120,125,120,125,120,100,90,		0,0,0,0,
						0,0,0,		90,100,120,130,110,130,120,100,90,		0,0,0,0,
						0,0,0,		90,110,110,120,100,120,110,110,90,		0,0,0,0,
						//���Ӻ���
						0,0,0,		90,100,100,110,100,110,100,100,90,		0,0,0,0,
						0,0,0,		80,90,100,100,90,100,100,90,80,			0,0,0,0,
						0,0,0,		80,80,90,90,80,90,90,80,80,				0,0,0,0,
						0,0,0,		70,75,75,70,50,70,75,75,70,				0,0,0,0,
						0,0,0,		60,70,75,70,60,70,75,70,60,				0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//�ڳ�
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,		160,170,160,150,150,150,160,170,160,	0,0,0,0,
						0,0,0,		170,180,170,190,250,190,170,180,170,	0,0,0,0,
						0,0,0,		170,190,200,220,240,220,200,190,170,	0,0,0,0,
						0,0,0,		180,220,210,240,250,240,210,220,180,	0,0,0,0,
						0,0,0,		180,220,210,240,250,240,210,220,180,	0,0,0,0,
						//���Ӻ���
						0,0,0,		180,220,210,240,250,240,210,220,180,	0,0,0,0,
						0,0,0,		170,190,180,220,240,220,200,190,170,	0,0,0,0,
						0,0,0,		170,180,170,170,160,170,170,180,170,	0,0,0,0,
						0,0,0,		160,170,160,160,150,160,160,170,160,	0,0,0,0,
						0,0,0,		150,160,150,160,150,160,150,160,150,	0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//����
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,		125,130,100,70,60,70,100,130,125,	0,0,0,0,
						0,0,0,		110,125,100,70,60,70,100,125,110,	0,0,0,0,
						0,0,0,		100,120,90,80,80,80,90,120,100,		0,0,0,0,
						0,0,0,		90,110,90,110,130,110,90,110,90,	0,0,0,0,
						0,0,0,		90,110,90,110,130,110,90,110,90,	0,0,0,0,
						//���Ӻ���
						0,0,0,		90,100,90,110,130,110,90,100,90,	0,0,0,0,
						0,0,0,		90,100,90,90,110,90,90,100,90,		0,0,0,0,
						0,0,0,		90,100,80,80,70,80,80,100,90,		0,0,0,0,
						0,0,0,		80,90,80,70,65,70,80,90,80,			0,0,0,0,
						0,0,0,		80,90,80,70,60,70,80,90,80,			0,0,0,0,
						
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
					},
					{//�ڱ�
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
						
						0,0,0,		10,10,10,20,25,20,10,10,10,		0,0,0,0,
						0,0,0,		25,30,40,50,60,50,40,30,25,		0,0,0,0,
						0,0,0,		25,30,30,40,40,40,30,20,25,		0,0,0,0,
						0,0,0,		20,25,25,30,30,30,25,25,20,		0,0,0,0,
						0,0,0,		15,20,20,20,20,20,20,20,15,		0,0,0,0,
						//���Ӻ���
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
	
	int []board=new int [256];	//��������������
	
	//��������,һά���������Ƿ���ڼ���λ��,��ά�������ӵļ�ֵ
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
	
	//�ƶ����������Ͻ�,˳ʱ�����
	int [] king={-16,1,16,-1};	//��,��,�� �ƶ�����
	int [] knight={-17,-15,17,15};//ʿ  �ƶ�����
	int [] elephant={-34,-30,34,30};//���ƶ�����
	int [] horse={-18,-33,-31,-14,18,33,31,14};//���ƶ�����
	int [][] soldier={{-16,1,-1},{16,1,-1}}; //�����ƶ�����
	
	int [] horseFeet={-1,-16,-16,1,1,16,16,-1};//���λ��
	int [] elephantFeet={-17,-15,17,15};//���λ��
	
	//fenֵ
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
	
	public void initBoard()	//��ʼ����,��������
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
	
	public void clearBoard()//�������,��������
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
	
	//��fen��char���͵�ֵת��Ϊint����
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
	
	//������������int���͵�ֵת��Ϊfen�е�char����
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
	
	public void fenToArray() 	//Fenת��Ϊ��������
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
	public void intToFen() //��������ת��ΪFenֵ
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
	
	public void saveMove(int from,int to,Move mv[])//�����ƶ�����
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
	public void saveMove(int from,int to,Move mv[],int num)//�����ƶ�����
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
	 
	public int Check(int nowSide)//���nowSideһ���Ƿ񱻽���
	{
		//wKing bKing �ֱ�����,��˫��˧(��)��λ��
		int wKing=piece[0][16];
		int bKing=piece[0][32];
		//����һ���з����ӵĻ�����С,����췽�Ļ�����С��16,�ڷ��Ļ�����С��32
		int LSideTag=(1-nowSide)*16+16;
		//qΪҪ����Ƿ񱻽��� ��˧�򽫵�λ��
		int q=piece[0][nowSide*16+16];
		//�Ƿ񱻽�����־ Ϊ1�Ǳ�����
		int r=0;
		//m��ʾ�ڼ�����ǰ���� ����5���� 2���� ֮���
		int m;
		//k��ʾ�ǵڼ�����λ  �������߰˷�,�����ķ�
		int k;
		//n��ʾĿ�����һ��λ��
		int n;
		//nf��ʾĿ��λ�õ���һ����,��������ʧ��,���б���
		int nf;
		//overFlag��ʾ�ڵķ�ɽ��־,Ϊ1���Ѿ���ɽ,Ϊ0ʱδ��ɽ
		int overFlag;
		
		int j,p;
		//��⽫˧�Ƿ����
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

		
		//������Ƿ񽫾�
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
		
		//��⳵�Ƿ񽫾�
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
		//������Ƿ񽫾�
		for(m=9;m<=10;m++)
		{
			overFlag=0;
			p=piece[0][LSideTag+m];
			if(p==0)
				continue;
			//����� ��ͬ��
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
		
		//�����Ƿ񽫾�
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
	
	//�����߷�����,pΪ���ĵ�ǰλ��
	
	public int genAllMove(Move mv[])//�������ӵ��ƶ�
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
	public void kingMove(Move mv[])//��˧�ƶ�
	{
		//kΪ���ߵķ�λ,nΪ��һ���ĵط�
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
	
	//����߷�����,pΪ���ĵ�ǰλ��
	public void horseMove(Move mv[])//����ƶ�
	{
		int nf;
		int k,n,m,p;
		//mΪ�ڼ������� ����˵�������� 
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
	
	//����߷�����
	public void elephentMove(Move mv[])//����ƶ�
	{
		int nf;
		int k,n,m,p;
		//mΪ�ڼ������� ����˵�������� 
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
	
	//ʿ���߷�����
	public void knightMove(Move mv[])//ʿ���ƶ�
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
	
	//�ڵ��߷�����
	public void cannonMove(Move mv[])//�ڵ��ƶ�
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
	
	//�����߷�����
	public void vehicleMove(Move mv[])//�����ƶ�
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
	
	//�����߷�����
	public void soldierMove(Move mv[])//�����ƶ�
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
	
	public boolean makeMove(Move m)//��ʼ�ƶ�
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
		
		//������������
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
		
		//������������
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
	public void unMakeMove()//�˳��ոյ��ƶ�
	{
		int from,dest,p;
		int pt,pc;
		stackTop--;
		ply--;
		side=1-side;
		
		from=moveStack[stackTop].from;
		dest=moveStack[stackTop].to;
		p=moveStack[stackTop].capture;
		
		//������������
		pc=board[from]=board[dest];
		board[dest]=p;
		pt = PieceNumToType[pc];
		if (pc>=32) 
		{
			pt += 7;
		}
		ZobristKey ^= ZobristTable[pt][from] ^ ZobristTable[pt][dest];
		ZobristKeyCheck ^= ZobristTableCheck[pt][from] ^ ZobristTableCheck[pt][dest];
		
		//������������
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
	
	public int AlphaBetaSearch(int depth,int alpha,int beta)//alphaBeta�㷨��ʵ��
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
		Move ma=new Move();//�洢��ʱ����߷�
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
	
	public int Eval()//�����ֵ�ж�
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
		int p;	//p:����λ��
		int n;	//��һ���������ߵ�λ��
		int m;	//���ȡ�����λ��
		int SideTag;		//���巽������16���ڷ�32
		int OverFlag;		//�ڵķ�ɽ��־
		int mvArray=num;

		SideTag = 16 + 16 * side;
		
		p = piece[0][SideTag];	//����λ��
		if(p==0)
			return 0;

		//�����߷�
		for(k=0; k<4; k++)//4������
		{
			n = p + king[k];	//nΪ�µĿ����ߵ���λ��
			if((LegalPosition[side][n] & PositionMask[0])!=0)	//����Ӧ�±�Ϊ0
			{
				if( board[n]==0 )	//Ŀ��λ����û������
				{
					saveMove(p, n, moveArray,mvArray);
					mvArray++;
				}
			}
		}

		//ʿ���߷�
		for(i=1; i<=2; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<4; k++)//4������
			{
				n = p + knight[k];	//nΪ�µĿ����ߵ���λ��
				if((LegalPosition[side][n] & PositionMask[1])!=0)	//ʿ��Ӧ�±�Ϊ1
				{
					if(  board[n]==0 )	//Ŀ��λ����û������
					{
						saveMove(p, n, moveArray,mvArray);
						mvArray++;
					}
				}
			}
		}

		//����߷�
		for(i=3; i<=4; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<4; k++)//4������
			{
				n = p + elephant[k];	//nΪ�µĿ����ߵ���λ��
				if((LegalPosition[side][n] & PositionMask[2])!=0)	//���Ӧ�±�Ϊ2
				{
					m = p + elephantFeet[k];
					if(board[m]==0)	//����λ��������ռ��
					{
						if(  board[n]==0 )	//Ŀ��λ����û������
						{
							saveMove(p, n, moveArray,mvArray);
							mvArray++;
						}
					}
				}
			}
		}
		
		//����߷�
		for(i=5; i<=6; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<8; k++)//8������
			{
				n = p + horse[k];	//nΪ�µĿ����ߵ���λ��
				if((LegalPosition[side][n] & PositionMask[3])!=0)	//���Ӧ�±�Ϊ3
				{
					m = p + horseFeet[k];
					if(board[m]==0)	//����λ��������ռ��
					{
						if(  board[n]==0 )	//Ŀ��λ����û������
						{
							saveMove(p, n, moveArray,mvArray);
							mvArray++;
						}
					}
				}
			}
		}

		//�����߷�
		for(i=7; i<=8; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<4; k++)	//4������
			{
				for(j=1; j<10; j++)	//��������8�������ߵ�λ�ã����������9��λ��
				{
					n = p + j * king[k];
					if((LegalPosition[side][n] & PositionMask[4])==0)	//����Ӧ�±�Ϊ4
						break;//�������λ��
					if( board[n] ==0)	//Ŀ��λ��������
					{
						saveMove(p, n, moveArray,mvArray);
						mvArray++;
					}
					else 	//Ŀ��λ����������,�˳��ڲ�ѭ��
						break;
				}
			}
		}

		//�ڵ��߷�
		for(i=9; i<=10; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<4; k++)	//4������
			{
				OverFlag = 0;
				for(j=1; j<10; j++)	//��������8�������ߵ�λ�ã����������9��λ��
				{
					n = p + j * king[k];
					if((LegalPosition[side][n] & PositionMask[5])==0)	//�ڶ�Ӧ�±�Ϊ5
						break;//�������λ��
					if( board[n]==0 )	//Ŀ��λ��������
					{
						if(OverFlag==0)	//δ��ɽ
						{
							saveMove(p, n, moveArray,mvArray);
							mvArray++;
						}
						//�ѷ�ɽ���������Զ���������һ��λ��
					}
					else//Ŀ��λ��������
					{
							break;	//���۳Բ����ӣ����˳��˷�������
					}
				}
			}	
		}

		//�����߷�
		for(i=11; i<=15; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<3; k++)//3������
			{
				n = p + soldier[side][k];	//nΪ�µĿ����ߵ���λ��
				if((LegalPosition[side][n] & PositionMask[6])!=0)	//����Ӧ�±�Ϊ6
				{
					if( board[n]==0 )	//Ŀ��λ����û�б�������
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
		int p;	//p:����λ��
		int n;	//��һ���������ߵ�λ��
		int m;	//���ȡ�����λ��
		int OpChess;	//���ԵĶԷ�����
		int value; //��ֵ
		int SideTag;		//���巽���췽16���ڷ�32
		int OpSideTag;		//�Է���־
		int OverFlag;		//�ڵķ�ɽ��־
		int mvArray=num;
		//move * mvArray = MoveArray;

		SideTag = 16 + 16 * side;
		OpSideTag = 48 - SideTag;
		
		p = piece[0][SideTag];	//����λ��
		if(p!=0)
			return 0;

		//�����߷�
		for(k=0; k<4; k++)//4������
		{
			n = p + king[k];	//nΪ�µĿ����ߵ���λ��
			if((LegalPosition[side][n] & PositionMask[0])!=0)	//����Ӧ�±�Ϊ0
			{
				OpChess = board[n];
				if( (OpChess & OpSideTag)!=0)	//Ŀ��λ�����жԷ�����
				{
					saveMove(p, n, MoveArray,mvArray);
					value = MvvValues[OpChess] - ((Protected(1 - side, n)!=0) ? 8 : 0) ;// ˧(��)�ļ�ֵ��8
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

		//ʿ���߷�
		for(i=1; i<=2; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<4; k++)//4������
			{
				n = p + knight[k];	//nΪ�µĿ����ߵ���λ��
				if((LegalPosition[side][n] & PositionMask[1])!=0)	//ʿ��Ӧ�±�Ϊ1
				{
					OpChess = board[n];
					if( (OpChess & OpSideTag)!=0)	//Ŀ��λ�����жԷ�����
					{
						saveMove(p, n, MoveArray,mvArray);
						value = MvvValues[OpChess] - ((Protected(1 - side, n)!=0) ? 2 : 0) ;// ʿ�ļ�ֵ��2
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

		//����߷�
		for(i=3; i<=4; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<4; k++)//4������
			{
				n = p + elephant[k];	//nΪ�µĿ����ߵ���λ��
				if((LegalPosition[side][n] & PositionMask[2])!=0)	//���Ӧ�±�Ϊ2
				{
					m = p + elephantFeet[k];
					if(board[m]==0)	//����λ��������ռ��
					{
						OpChess = board[n];
						if((OpChess & OpSideTag)!=0)	//Ŀ��λ�����жԷ�����
						{
							saveMove(p, n, MoveArray,mvArray);
							value = MvvValues[OpChess] - ((Protected(1 - side, n)!=0) ? 2 : 0) ;// ��ļ�ֵ��2
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
		
		//����߷�
		for(i=5; i<=6; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<8; k++)//8������
			{
				n = p + horse[k];	//nΪ�µĿ����ߵ���λ��
				if((LegalPosition[side][n] & PositionMask[3])!=0)	//���Ӧ�±�Ϊ3
				{
					m = p + horseFeet[k];
					if(board[m]==0)	//����λ��������ռ��
					{
						OpChess = board[n];
						if( (OpChess & OpSideTag)!=0)	//Ŀ��λ�����жԷ�����
						{
							saveMove(p, n, MoveArray,mvArray);
							value = MvvValues[OpChess] - ((Protected(1 - side, n)!=0) ? 2 : 0) ;// ��ļ�ֵ��4
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

		//�����߷�
		for(i=7; i<=8; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<4; k++)	//4������
			{
				for(j=1; j<10; j++)	//��������8�������ߵ�λ�ã����������9��λ��
				{
					n = p + j * king[k];
					if((LegalPosition[side][n] & PositionMask[4])==0)	//����Ӧ�±�Ϊ4
						break;//�������λ��
					OpChess = board[n];
					if(OpChess==0 )	//Ŀ��λ�������ӣ���������
					{
					}
					else if ( (OpChess & SideTag)!=0)	//Ŀ��λ�����б�������
						break;
					else	//Ŀ��λ�����жԷ�����
					{
						saveMove(p, n, MoveArray,mvArray);
						value = MvvValues[OpChess] - ((Protected(1 - side, n)!=0) ? 2 : 0) ;// ���ļ�ֵ��6
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

		//�ڵ��߷�
		for(i=9; i<=10; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<4; k++)	//4������
			{
				OverFlag = 0;
				for(j=1; j<10; j++)	//��������8�������ߵ�λ�ã����������9��λ��
				{
					n = p + j * king[k];
					if((LegalPosition[side][n] & PositionMask[5])==0)	//�ڶ�Ӧ�±�Ϊ5
						break;//�������λ��
					OpChess = board[n];
					if(OpChess==0 )	//Ŀ��λ��������
					{
						//���������Զ���������һ��λ��
					}
					else//Ŀ��λ��������
					{
						if (OverFlag==0)	//δ��ɽ���÷�ɽ��־
							OverFlag = 1;
						else	//�ѷ�ɽ
						{
							if((OpChess & OpSideTag)!=0)//�Է�����
							{
								saveMove(p, n, MoveArray,mvArray);
								value = MvvValues[OpChess] - ((Protected(1 - side, n)!=0) ? 2 : 0) ;// �ڵļ�ֵ��4
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
							break;	//���۳Բ����ӣ����˳��˷�������
						}
					}
				}
			}	
		}

		//�����߷�
		for(i=11; i<=15; i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			for(k=0; k<3; k++)//3������
			{
				n = p + soldier[side][k];	//nΪ�µĿ����ߵ���λ��
				if((LegalPosition[side][n] & PositionMask[6])!=0)	//����Ӧ�±�Ϊ6
				{
					OpChess = board[n];
					if( (OpChess & OpSideTag)!=0)	//Ŀ��λ�����жԷ�����
					{
						saveMove(p, n, MoveArray,mvArray);
						value = MvvValues[OpChess] - ((Protected(1 - side, n)!=0) ? 2 : 0) ;// ���ļ�ֵ��2
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
		int SideTag = 16 + lSide * 16;	//�˴���ʾlSide����ֵ
		int i,k;	//ѭ������
		int PosAdd;	//λ������
		int n;//��һ���������ߵ�λ��
		int m;//����λ��

		if (lSide == 0 ? (dst & 0x80) != 0 : (dst & 0x80) == 0) //�ж������Ƿ����,δ������˧,��,�󱣻�
		{
			//����Ƿ񱻽�����
			p = piece[0][SideTag];
			if(p!=0&& p!=dst)
			{
				for(k=0; k<4; k++)//4������
				{
					n = p + horse[k];	//nΪ�µĿ����ߵ���λ��
					if(n!=dst)
						continue;

					if((LegalPosition[lSide][n] & PositionMask[0])!=0)	//����Ӧ�±�Ϊ0
					{
						return 1;
					}
				}
			}

			//����Ƿ�ʿ����
			for(i=1;i<=2;i++)
			{
				p = piece[0][SideTag+i];
				if(p==0)
					continue;
				if(p==dst)
					continue;
				for(k=0; k<4; k++)//4������
				{
					n = p + horse[k];	//nΪ�µĿ����ߵ���λ��
					if(n!=dst)
						continue;

					if((LegalPosition[lSide][n] & PositionMask[1])!=0)	//ʿ��Ӧ�±�Ϊ1
					{
						return 1;
					}
				}	
			}
			//����Ƿ��ౣ��
			for(i=3;i<=4;i++)
			{
				p = piece[0][SideTag+i];
				if(p==0)
					continue;
				if(p==dst)
					continue;
				for(k=0; k<4; k++)//4������
				{
					n = p + horse[k];	//nΪ�µĿ����ߵ���λ��
					if(n!=dst)
						continue;

					if((LegalPosition[lSide][n] & PositionMask[2])!=0)	//���Ӧ�±�Ϊ2
					{
						m = p + elephantFeet[k];
						if(board[m]==0)	//����λ��������ռ��
						{
							return 1;
						}
					}
				}	
			}
		}


		//����Ƿ�����
		for(i=5;i<=6;i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			if(p==dst)
				continue;
			for(k=0; k<8; k++)//8������
			{
				n = p + horse[k];	//nΪ�µĿ����ߵ���λ��
				if(n!=dst)
					continue;

				if((LegalPosition[lSide][n] & PositionMask[3])==0)	//����Ӧ�±�Ϊ3
				{
					m = p + horseFeet[k];
					if(board[m]==0)	//����λ��������ռ��
					{
						return 1;
					}
				}
			}
		}
		
		//��⽫�Ƿ񱻳�����
		r=1;
		for(i=7;i<=8;i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			if(p==dst)
				continue;
			if(p%16 == dst%16)	//��ͬһ������
			{
				PosAdd = (p>dst?-16:16);
				for(p=p+PosAdd; p!=dst; p = p+PosAdd)
				{
					if((board[p])!=0)	//�����м����Ӹ���
					{
						r=0;
						break;
					}
				}
				if(r!=0)
					return r;
			}
			else if(p/16 ==dst/16)	//��ͬһ������
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
		
		//��⽫�Ƿ��ڹ���
		int OverFlag = 0;	//��ɽ��־
		for(i=9;i<=10;i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			if(p==dst)
				continue;
			if(p%16 == dst%16)	//��ͬһ������
			{
				PosAdd = (p>dst?-16:16);
				for(p=p+PosAdd; p!=dst; p = p+PosAdd)
				{
					if(board[p]!=0)
					{
						if(OverFlag==0)	//��һ��
							OverFlag = 1;
						else			//������
						{
							OverFlag = 2;
							break;
						}
					}
				}
				if(OverFlag==1)
					return 1;
			}
			else if(p/16 ==dst/16)	//��ͬһ������
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

		//��⽫�Ƿ񱻱�����
		for(i=11;i<=15;i++)
		{
			p = piece[0][SideTag + i];
			if(p==0)
				continue;
			if(p==dst)
				continue;
			for(k=0; k<3; k++)//3������
			{
				n = p + soldier[lSide][k];	//nΪ�µĿ����ߵ���λ��
				if((n==dst) && (LegalPosition[lSide][n] & PositionMask[6])!=0)	//��ʿ����Ӧ�±�Ϊ6
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












