#-*- coding: utf-8 -*-


# 학교에서 X 했다. ->> 학교에서 & 했다를 찾아서  ->> count X에 올수 있는 것들 -> X분류 ->>확률 ㄱㄱ

import numpy as np
import sys
def splitgram(f):
    gram=['gram']
    while True:
        line = f.readline()
        if not line: break
        data=np.array(line.split())
        for i in range(0,len(data)):
            if len(data[i])>1 :
                for j in range  (0,len(data[i])):
                    gram.append(data[i][j])
            else:
                gram.append(data[i])

    return gram


def Ngram(data,number):
    syllable=splitgram(data)
    table=["Syllable"]
    count=["Count"]
    n=1
    tmp=""
    for i in range(1,len(syllable)-number+1):
        tmp=""
        for j in range(0,number):
            if i+j == len(syllable) :
                break
            else:
                tmp=tmp+syllable[i+j]
        for k in range(0,n):

            if tmp!=table[k]:
                if k==n-1:
                    table.append(tmp)
                    count.append(1)
                    n=n+1
                    break
                continue
            elif tmp==table[k]:
                count[k]=count[k]+1
                break

    return table,count


def Nword(f,number):
    data=f.read().split()
    table=["word"]
    count=["Count"]
    n=1

    for i in range(0,len(data)-number+1):
        tmp=''
        for j in range(0,number):
            if i+j == len(data) :
                break
            else:
                if  j==number-1:
                    tmp=tmp+data[i+j]
                else:
                    tmp=tmp+data[i+j]+" "
        for k in range(0,n):
            if tmp!=table[k]:
                if k==n-1:
                    table.append(tmp)
                    count.append(1)
                    n=n+1
                    break
                continue
            elif tmp==table[k]:
                count[k]=count[k]+1
                break

    return table,count

def SyllableORWord(a):
    count=0

    for i in range(0,len(a)):
        if a[i]!=" ":
            continue
        else:
            count+=1

    if count==0: #음절
        return count
    else:
        return count
def Result(f):
    total=0
    print ("----input-----")
    print("1.음절, 2,단어 : ")
    choise=input()
    a=input()

    if choise==1: #음절 찾기
        print("----processing start----")
        c=SyllableORWord(a)

        number=len(a)
        syllable=np.array(Ngram(f,number))
        print(syllable)
        for i in range(1,len(syllable[0])):
            total=total+int(syllable[1][i])
            if a==syllable[0][i]:
                print(syllable[0][i],"--",syllable[1][i],"개 ","전체:",total, "확률: ",(int(syllable[1][i])/total))
                break
    else:   #단어
        print("----processing start----")
        c=SyllableORWord(a)

        number=c+1
        word=np.array(Nword(f,number))
        for j in range(1,len(word[0])):
            total=total+int(word[1][j] )
        print(word)
        for i in range(1,len(word[0])):

            if a==word[0][i]:
                print(word[0][i],"--",word[1][i],"개 ","전체:",total ,"확률: ",(int(word[1][i])/total))
                break


def main(arvg):
    f=open('test.txt','r',encoding='utf-8')
    Result(f)

    # a=input()
    # c=SyllableORWord(a)
    # if c==0: #음절 찾기
    #     number=len(a)
    #     syllable=np.array(Ngram(f,number))
    #
    #     for i in range(1,len(syllable[0])):
    #         if a==syllable[0][i]:
    #             print(syllable[0][i],"--",syllable[1][i],"개")
    #             break
    # else:   #단어
    #     number=c+1
    #     word=np.array(Nword(f,number))
    #
    #     for i in range(1,len(word[0])):
    #         if a==word[0][i]:
    #             print(word[0][i],"--",word[1][i],"개")
    #             break

    f.close()
    print("----proccessing end----")
if __name__=='__main__':
    main(sys.argv)