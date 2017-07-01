#-*- coding: utf-8 -*-



import numpy as np
import sys
import time as t
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
    print("1.음절, 2.단어, 3.'x' 찾기 : ")
    choice=input()


    if choice=='1': #음절 찾기
        a=input()
        print("----processing start----")
        c=SyllableORWord(a)

        number=len(a)
        syllable=np.array(Ngram(f,number))
        #print(syllable)
        for i in range(1,len(syllable[0])):
            total=total+int(syllable[1][i])
            if a==syllable[0][i]:
                print(syllable[0][i],"--",syllable[1][i],"개 ","전체:",total, "확률: ",(int(syllable[1][i])/total))
                break
    elif choice=='2' or choice=='3':   #단어
        a=input()
        print("----processing start----")
        c=SyllableORWord(a)

        number=c+1
        word=np.array(Nword(f,number))
        for j in range(1,len(word[0])):
            total=total+int(word[1][j] )
        #print(word)
        for i in range(1,len(word[0])):

            if a==word[0][i]:
                print(word[0][i],"--",word[1][i],"개 ","전체:",total ,"확률: ",(int(word[1][i])/total))
                break
        if choice=='3':
            prediction(a,word,total)
    else:
        print("error")


def prediction(a,word,total):
    #print("----prediction----")
    solution=[]
    candidate=[]
    probability=[]
    tmp=''
    m=0
    count=0

    for i in range(0,len(a)):
        count+=1
        if a[i]==' ':
            for j in range(i-count+1,i):
                tmp=tmp+a[j]
            count=0
            solution.append(tmp)
            tmp=''
        if i==len(a)-1:
            for j in range(i-count+1,i+1):
                tmp=tmp+a[j]
            solution.append(tmp)
    print("sol=",solution)
    for i in range(1,len(word[0])):
        temp=word[0][i].split()

        for j in range(0,len(solution)):
            if solution[j]==temp[j] or solution[j]=='x':
                if solution[j]=='x':
                    m=j
                if j!=len(solution)-1:
                    continue
            else:
                break

            candidate.append([temp[m],int(word[1][i])])

    candidate=selected_sort(candidate)
    for i in range(0,len(candidate)):

        print("x:" ,candidate[i][0],"    확률:",candidate[i][1]/total)


def selected_sort(candidate):
  for i in range( len(candidate)-1 ):
    max = candidate[i][1]
    max_w=candidate[i][0]
    maxindex = i

    for j in range( i+1, len(candidate) ):
      if max < candidate[j][1]:
        max = candidate[j][1]
        max_w=candidate[j][0]
        maxindex = j

    candidate[maxindex][1] = candidate[i][1]
    candidate[maxindex][0]=candidate[i][0]
    candidate[i][1] = max
    candidate[i][0]=max_w

  return candidate


def main(arvg):
    f=open('test.txt','r',encoding='utf-8')
    start=t.time()
    Result(f)

    f.close()
    end=t.time()
    print("----proccessing end----")
    print('걸린 시간 :' ,end-start)
if __name__=='__main__':
    main(sys.argv)

